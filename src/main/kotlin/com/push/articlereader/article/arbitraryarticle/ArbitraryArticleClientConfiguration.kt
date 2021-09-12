package com.push.articlereader.article.arbitraryarticle

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ArbitraryArticleClientConfiguration {

    @Bean
    fun arbitraryArticleClient(): WebClient {
        return WebClient.builder()
            .defaultHeader("cache-control", "no-cache")
            .exchangeStrategies(
                ExchangeStrategies.builder()
                .codecs { configurer ->
                    configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024) }
                    .build()
            )
            .build()
    }
}