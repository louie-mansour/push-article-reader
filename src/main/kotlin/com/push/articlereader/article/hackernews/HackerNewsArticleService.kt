package com.push.articlereader.article.hackernews

import com.push.articlereader.article.IArticleService
import com.push.articlereader.article.hackernews.dto.HackerNewsStoryDto
import com.push.articlereader.article.arbitraryarticle.ArbitraryArticleRepo
import com.push.articlereader.article.arbitraryarticle.dto.ArbitraryArticleDto
import com.push.articlereader.model.Article
import com.push.articlereader.model.ArticleSource
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@Component
class HackerNewsArticleService(
    private val hackerNewsRepo: HackerNewsRepo,
    private val unstructuredArticleService: ArbitraryArticleRepo) : IArticleService {

    override fun getArticles(): Flux<Article> {
        println("Retrieving articles from hacker news")
        return hackerNewsRepo.getTopStories()
            .flatMap { story ->
                unstructuredArticleService.getArticle(story.url)
                    .map { transform(it, story) }
                    .onErrorContinue { err, i -> println("An error occurred retrieving article: $i, $err") }

            }
            .doOnNext { println("Retrieved article referenced by HackerNews: ${it.url}") }
            .onErrorContinue { err, i -> println("An error occurred from Hacker news while retrieving article: $i, $err") }
    }

    private fun transform(arbitraryArticleDto: ArbitraryArticleDto, hackerNewsStoryDto: HackerNewsStoryDto): Article {
        return Article(
            articleSource = ArticleSource.HACKER_NEWS,
            url = arbitraryArticleDto.url,
            author = "unknown",
            titleForDisplay = hackerNewsStoryDto.title,
            titleForAnalysis = "${hackerNewsStoryDto.title} ${arbitraryArticleDto.title}",
            content = arbitraryArticleDto.bodyText,
            comments = emptyList(),
            publishedInstant = Instant.ofEpochMilli(hackerNewsStoryDto.time.toLong()),
            upVotes = hackerNewsStoryDto.score,
            uploadedInstant = Instant.ofEpochMilli(hackerNewsStoryDto.time.toLong())
        )
    }
}