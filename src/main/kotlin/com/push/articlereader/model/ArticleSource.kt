package com.push.articlereader.model

enum class ArticleSource {
    HACKER_NEWS,
    KOTLIN_BLOG;

    companion object {
        fun create(s: String?): Set<ArticleSource> {
            return when(s) {
                "hacker-news" -> setOf(HACKER_NEWS)
                "kotlin-blog" -> setOf(KOTLIN_BLOG)
                "all" -> setOf(HACKER_NEWS, KOTLIN_BLOG)
                else -> throw IllegalArgumentException("Unknown article sourceId: $s")
            }
        }
    }
}