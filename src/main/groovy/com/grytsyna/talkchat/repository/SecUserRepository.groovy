package com.grytsyna.talkchat.repository

import groovy.transform.CompileStatic

import com.grytsyna.talkchat.entity.SecUser
import org.springframework.data.mongodb.repository.MongoRepository

/**
* SecUserRepository
*/
@CompileStatic
interface SecUserRepository extends MongoRepository<SecUser, String> {

    Optional<SecUser> findByEmail(String email)

    boolean existsByEmail(String email)

}
