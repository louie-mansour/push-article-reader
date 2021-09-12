package com.push.articlereader.article

import com.push.articlereader.article.hackernews.HackerNewsArticleService
import com.push.articlereader.model.Article
import com.push.articlereader.model.ArticleSource
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.lang.RuntimeException

@Component
class ArticleFacade(private val hackerNewsFacade: HackerNewsArticleService) {

    fun getArticles(articleSource: ArticleSource): Flux<Article> {
        return when(articleSource) {
            ArticleSource.HACKER_NEWS -> hackerNewsFacade.getArticles()
            ArticleSource.KOTLIN_BLOG -> throw RuntimeException("Not implemented")
        }
    }
}