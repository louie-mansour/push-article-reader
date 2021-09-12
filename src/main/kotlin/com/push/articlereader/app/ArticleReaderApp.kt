package com.push.articlereader.app

import com.push.articlereader.article.ArticleFacade
import com.push.articlereader.event.ReadEventService
import com.push.articlereader.model.Article
import com.push.articlereader.model.ArticleSource
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class ArticleReaderApp(
    private val articleFacade: ArticleFacade,
    private val eventService: ReadEventService) {

    fun migrateArticles(articleSource: ArticleSource): Flux<Article> {
        return articleFacade.getArticles(articleSource)
            .flatMap { eventService.publishArticle(it) }
    }
}