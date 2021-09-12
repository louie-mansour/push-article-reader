package com.push.articlereader.event

import com.push.articlereader.event.dto.ReadArticleEvent
import com.push.articlereader.model.Article
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ReadEventService(private val eventClient: EventClient) {

    fun publishArticle(article: Article): Mono<Article> {
        return Mono.just(article)
            .map { transform(it) }
            .flatMap { eventClient.publishReadEvent(it) }
            .thenReturn(article)
    }

    private fun transform(article: Article): ReadArticleEvent {
        return ReadArticleEvent(
            articleSource = article.articleSource.name,
            url = article.url,
            titleForDisplay = article.titleForDisplay,
            titleForAnalysis = article.titleForAnalysis,
            content = article.content,
            uploadedInstant = article.uploadedInstant.epochSecond.toInt(),
            comments = article.comments,
            readInstant = article.readInstant.epochSecond.toInt(),
            publishedInstant = article.publishedInstant?.epochSecond?.toInt(),
            author = article.author,
            upVotes = article.upVotes,
            downVotes = article.downVotes,
            numberOfReads = article.numberOfReads
        )
    }
}