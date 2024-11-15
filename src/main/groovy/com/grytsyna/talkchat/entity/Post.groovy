package com.grytsyna.talkchat.entity

import groovy.transform.CompileStatic
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
* Post
*/
@CompileStatic
@Document(collection = 'post')
class Post {

    @Id
    String id
    String authorId
    String content
    List<String> likes = []
    List<Comment> comments = []

    void addLike(String userId) {
        if (!likes.contains(userId)) {
            likes.add(userId)
        }
    }

    void removeLike(String userId) {
        likes.remove(userId)
    }

    void addComment(Comment comment) {
        comments.add(comment)
    }

    void updateContent(String newContent) {
        content = newContent
    }

}
