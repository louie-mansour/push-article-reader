package com.push.articlereader.article.arbitraryarticle

import com.push.articlereader.article.arbitraryarticle.dto.ArbitraryArticleDto
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class ArbitraryArticleRepo(private val arbitraryArticleClient: WebClient) {

    fun getArticle(url: String): Mono<ArbitraryArticleDto> {
        return arbitraryArticleClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(String::class.java)
            .map { transform(it, url)}
    }

    private fun transform(html: String, url: String): ArbitraryArticleDto {
        val htmlDoc = Jsoup.parse(html)

        return ArbitraryArticleDto(
            url = url,
            title = htmlDoc.title(),
            headers = htmlDoc.select("h1").text() + htmlDoc.select("h2"),
            bodyText = htmlDoc.text()
        )
    }
}