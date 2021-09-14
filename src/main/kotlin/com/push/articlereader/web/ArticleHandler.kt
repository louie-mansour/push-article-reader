package com.push.articlereader.web

import com.push.articlereader.app.ArticleReaderApp
import com.push.articlereader.model.ArticleSource
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.queryParamOrNull
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux

@Configuration
class ArticleHandler(private val articleReaderApp: ArticleReaderApp) {

    fun test(): Mono<ServerResponse> {
        return ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(""))
    }

    fun migrateArticles(serverRequest: ServerRequest): Mono<ServerResponse> {
        val articleSourcesSet = serverRequest
            .queryParamOrNull("sources")
            ?.split(",")
            ?.flatMap { ArticleSource.create(it) }
            ?.distinct()
            ?: throw IllegalArgumentException("Must include query parameter 'sources'")

        return articleSourcesSet
            .toFlux()
            .flatMap { articleReaderApp.migrateArticles(it) }
            .collectList()
            .flatMap {
                ok().contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(""))
            }
    }
}