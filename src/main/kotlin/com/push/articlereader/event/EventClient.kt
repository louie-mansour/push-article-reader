package com.push.articlereader.event

import com.push.articlereader.event.dto.ReadArticleEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class EventClient {

    fun publishReadEvent(readArticleEvent: ReadArticleEvent): Mono<ReadArticleEvent> {
        return Mono.just(readArticleEvent)
    }
}