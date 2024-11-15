package com.grytsyna.talkchat.configuration

import groovy.transform.CompileStatic

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.NonNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import com.grytsyna.talkchat.service.SecUserService

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/**
* JwtAuthenticationFilter
*/
@CompileStatic
@Component
class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = 'Bearer '
    public static final String HEADER_NAME = 'Authorization'
    private final JwtConfiguration jwtConfiguration
    private final SecUserService secUserService

    @Autowired
    JwtAuthenticationFilter(JwtConfiguration jwtConfiguration, SecUserService secUserService) {
        this.jwtConfiguration = jwtConfiguration
        this.secUserService = secUserService
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_NAME)
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
            filterChain.doFilter(request, response)
            return
        }

        String jwt = authHeader.substring(BEARER_PREFIX.length())
        String email = jwtConfiguration.extractUserName(jwt)

        if (StringUtils.isNotEmpty(email) && SecurityContextHolder?.context?.authentication == null) {
            UserDetails userDetails = secUserService
                    .userDetailsService()
                    .loadUserByUsername(email)

            if (jwtConfiguration.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext()

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                )

                authToken.details = new WebAuthenticationDetailsSource().buildDetails(request)
                context.authentication = authToken
                SecurityContextHolder.context = context
            }
        }
        filterChain.doFilter(request, response)
    }

}
