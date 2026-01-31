package com.analystlab.app.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class ModuleDomainModel(
    val id: String,
    val name: String,
    val description: String,
    val duration: String,
    @SerialName("activities_count")
    val activitiesCount: Int,
    val completed: Boolean = false,
    val activities: List<ActivityDomainModel> = emptyList()
)
