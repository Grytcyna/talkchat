package com.grytsyna.talkchat.configuration

import groovy.transform.CompileStatic

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
* MongoConfig
*/
@CompileStatic
@Configuration
class MongoConfig {

    @Value('${spring.data.mongodb.host}')
    String host

    @Value('${spring.data.mongodb.port}')
    String port

    @Value('${spring.data.mongodb.database}')
    String db

    @Value('${spring.data.mongodb.username}')
    String username

    @Value('${spring.data.mongodb.password}')
    String password

    @Value('${spring.data.mongodb.authentication-database}')
    String auth

    @Bean
    MongoClient mongoClient() {
        String connectionString = "mongodb://${username}:${password}@${host}:${port}/${db}?authSource=${auth}"
        return MongoClients.create(connectionString)
    }

}
