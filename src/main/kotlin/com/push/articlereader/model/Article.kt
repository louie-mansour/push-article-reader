package com.push.articlereader.model

import java.time.Instant
import java.time.Instant.now

data class Article(
    val articleSource: ArticleSource,
    val url: String,
    val titleForDisplay: String,
    val titleForAnalysis: String,
    val content: String,
    val uploadedInstant: Instant,
    val comments: List<String>,
    val publishedInstant: Instant? = null,
    val author: String? = null,
    val upVotes: Int? = null,
    val downVotes: Int? = null,
    val numberOfReads: Int? = null,
    val readInstant: Instant = now()
)