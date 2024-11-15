package com.grytsyna.talkchat.controller

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
* Health check Controller
*/
@CompileStatic
@RestController
class StatusController {

    @Value('${status.message:alive}')
    private String status

    @GetMapping('/status')
    String getStatus() {
        return status
    }

}
