package com.grytsyna.talkchat

import groovy.transform.CompileStatic
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import io.github.cdimascio.dotenv.Dotenv;

/**
* TalkchatApplication
*/
@CompileStatic
@SpringBootApplication
class TalkchatApplication {

    static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load()
        dotenv.entries().forEach(entry -> System.setProperty(entry.key, entry.value));
        SpringApplication.run(TalkchatApplication, args)
    }

}
