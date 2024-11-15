package com.grytsyna.talkchat.entity.enums

import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority

/**
* Role
*/
@CompileStatic
enum Role implements GrantedAuthority {

    ROLE_ADMIN,
    ROLE_USER,
    ROLE_UNAUTHORIZED

    @Override
    String getAuthority() {
        return name()
    }

}
