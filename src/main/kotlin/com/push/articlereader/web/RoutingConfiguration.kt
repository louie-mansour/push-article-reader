package com.push.articlereader.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class RoutingConfiguration(private val articleHandler: ArticleHandler) {
    @Bean
    fun articleRouter() = router {
        (accept(MediaType.APPLICATION_JSON)).nest {
            POST("/migrate").invoke { articleHandler.migrateArticles(it) }
            GET("/test").invoke { articleHandler.test() }
        }
    }
}