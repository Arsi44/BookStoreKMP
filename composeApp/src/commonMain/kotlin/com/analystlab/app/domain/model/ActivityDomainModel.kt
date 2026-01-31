package com.analystlab.app.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class ActivityDomainModel(
    val id: String,
    val name: String,
    val type: String,
    val completed: Boolean = false
)
