package com.push.articlereader.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import com.push.articlereader.event.dto.ReadArticleEvent
import com.push.articlereader.model.Article
import org.springframework.cloud.gcp.pubsub.core.publisher.PubSubPublisherTemplate
import org.springframework.cloud.gcp.pubsub.reactive.PubSubReactiveFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
    class ReadEventPublisher(private val pubSubTemplate: PubSubPublisherTemplate, private val pubSubReactiveFactory: PubSubReactiveFactory) {

    fun publishArticle(article: Article): Mono<Article> {
        println("Creating read article event: ${article.url}")
        return Mono.just(article)
            .map { pubSubTemplate.publish(READ_EVENT_TOPIC_NAME, transform(it) ) }
            .then(Mono.just(article))
    }

    private fun transform(article: Article): PubsubMessage {
        val json = objectWriter.writeValueAsString(
            ReadArticleEvent(
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
        )
        println("json is $json")
        return PubsubMessage.newBuilder()
            .putAttributes("url", article.url)
            .setData(
                ByteString.copyFromUtf8(json)
            )
            .build()
    }

    companion object {
        const val READ_EVENT_TOPIC_NAME = "article-read-events"
        private val objectWriter = ObjectMapper().writer()
    }
}