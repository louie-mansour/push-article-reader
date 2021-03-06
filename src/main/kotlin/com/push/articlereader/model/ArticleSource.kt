package com.push.articlereader.model

enum class ArticleSource(name: String) {
    HACKER_NEWS("hacker-news"),
    KOTLIN_BLOG("kotlin-blog");

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