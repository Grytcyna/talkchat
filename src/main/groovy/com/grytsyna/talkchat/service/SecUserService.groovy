package com.grytsyna.talkchat.service

import groovy.transform.CompileStatic

import com.grytsyna.talkchat.entity.SecUser
import com.grytsyna.talkchat.repository.SecUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
* SecUserService
*/
@CompileStatic
@Service
class SecUserService {

    private final SecUserRepository secUserRepository

    @Autowired
    SecUserService(SecUserRepository secUserRepository) {
        this.secUserRepository = secUserRepository
    }

    SecUser save(SecUser user) {
        return secUserRepository.save(user)
    }

    SecUser addNewUser(SecUser user) {
        if (secUserRepository.existsByEmail(user.email)) {
            throw new BadCredentialsException('User with this email already exists')
        }
        return save(user)
    }

    SecUser getByEmail(String email) {
        return secUserRepository.findByEmail(email)
                    .orElseThrow { -> new UsernameNotFoundException('Sec User not found by email') }
    }

    SecUser getById(String id) {
        return secUserRepository.findById(id)
                    .orElseThrow { -> new UsernameNotFoundException('Sec User not found by id') }
    }

    boolean userExistsByEmail(String email) {
        return secUserRepository.existsByEmail(email)
    }

    UserDetailsService userDetailsService() {
        return this::getByEmail
    }

    SecUser getCurrentUser() {
        if (SecurityContextHolder?.context?.authentication.is(AnonymousAuthenticationToken)) {
            return null
        }

        Object principal = SecurityContextHolder?.context?.authentication?.principal

        if (principal.is(SecUser)) {
            return principal as SecUser
        }

        throw new IllegalStateException('Principal must be an instance of SecUser')
    }

}
