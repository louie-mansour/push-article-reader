package com.push.articlereader.app

import com.push.articlereader.article.ArticleFacade
import com.push.articlereader.event.ReadEventPublisher
import com.push.articlereader.model.Article
import com.push.articlereader.model.ArticleSource
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class ArticleReaderApp(
    private val articleFacade: ArticleFacade,
    private val eventService: ReadEventPublisher) {

    fun migrateArticles(articleSource: ArticleSource): Flux<Article> {
        println("Starting article migration")
        return articleFacade.getArticles(articleSource)
            .flatMap { eventService.publishArticle(it) }
    }
}