package com.push.articlereader.article.hackernews

import com.push.articlereader.article.hackernews.dto.HackerNewsStoryDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import java.time.Duration

@Component
class HackerNewsRepo(private val hackerNewsClient: WebClient) {
    fun getTopStories(): Flux<HackerNewsStoryDto> {
        return getTopStoryIds()
            .delayElements(Duration.ofMillis(DELAY_BETWEEN_ARTICLES_MILLIS))
            .doOnNext { println("start: $it")}
            .flatMap { getStory(it) }
            .doOnNext { println("end:  ${it.id}")}
            .filter { it.type == "story" && !it.dead && !it.deleted && it.url.isNotBlank() }
    }

    private fun getTopStoryIds(): Flux<Int> {
        return hackerNewsClient
            .get()
            .uri(TOP_STORIES_URI)
            .retrieve()
            .bodyToMono(object: ParameterizedTypeReference<List<Int>>() {})
            .flatMapMany { it.toFlux() }
            .take(ARTICLE_LIMIT)
    }

    private fun getStory(id: Int): Mono<HackerNewsStoryDto> {
        return hackerNewsClient
            .get()
            .uri("$ARTICLE_URI/$id.json")
            .retrieve()
            .bodyToMono(HackerNewsStoryDto::class.java)
    }

    companion object {
        private const val TOP_STORIES_URI = "/v0/topstories.json"
        private const val ARTICLE_URI = "/v0/item"
        private const val ARTICLE_LIMIT = 200L
        private const val DELAY_BETWEEN_ARTICLES_MILLIS = 50L
    }
}