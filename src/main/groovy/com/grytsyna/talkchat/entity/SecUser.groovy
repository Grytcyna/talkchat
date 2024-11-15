package com.grytsyna.talkchat.entity

import groovy.transform.CompileStatic
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import com.grytsyna.talkchat.entity.enums.Role

/**
* SecUser
*/
@CompileStatic
@Document(collection = 'sec_user')
class SecUser implements UserDetails {

    private static final long serialVersionUID = 8294729384723948290L

    @Id
    String id
    String email
    String password
    Role role

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return [new SimpleGrantedAuthority(role.name())]
    }

    @Override
    String getUsername() {
        return email
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }

}
