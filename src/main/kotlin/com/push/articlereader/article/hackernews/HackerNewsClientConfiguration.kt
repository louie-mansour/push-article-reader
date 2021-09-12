package com.push.articlereader.article.hackernews

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class HackerNewsClientConfiguration {

    @Bean
    fun hackerNewsClient(): WebClient {
        return WebClient.builder()
            .defaultHeader("cache-control", "no-cache")
            .defaultHeader("content-type", "application/json")
            .baseUrl(HACKER_NEWS_BASE_URL)
            .build()
    }

    companion object {
        private const val HACKER_NEWS_BASE_URL = "https://hacker-news.firebaseio.com"
    }
}