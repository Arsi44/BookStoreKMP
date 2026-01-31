package com.analystlab.app.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class StatsDomainModel(
    @SerialName("progress_percent")
    val progressPercent: Int = 0,
    @SerialName("sql_queries_count")
    val sqlQueriesCount: Int = 0,
    @SerialName("diagrams_count")
    val diagramsCount: Int = 0
)
