package com.grytsyna.talkchat.entity

import groovy.transform.CompileStatic
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
* Comment
*/
@CompileStatic
@Document(collection = 'comment')
class Comment {

    @Id
    String id
    String postId
    String authorId
    String content

}
