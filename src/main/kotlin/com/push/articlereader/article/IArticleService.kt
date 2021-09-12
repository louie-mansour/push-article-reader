package com.push.articlereader.article

import com.push.articlereader.model.Article
import reactor.core.publisher.Flux

interface IArticleService {
    fun getArticles(): Flux<Article>
}