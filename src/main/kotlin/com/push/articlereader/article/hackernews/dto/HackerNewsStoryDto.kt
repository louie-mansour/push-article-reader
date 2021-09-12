package com.push.articlereader.article.hackernews.dto

data class HackerNewsStoryDto(
    val by: String,
    val descendants: Int,
    val id: Int,
    val kids: List<Int>? = emptyList(),
    val score: Int,
    val time: Int,
    val title: String,
    val type: String,
    val url: String = "",
    val deleted: Boolean = false,
    val dead: Boolean = false
)