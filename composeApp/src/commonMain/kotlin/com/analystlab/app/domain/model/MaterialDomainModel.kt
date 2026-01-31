package com.analystlab.app.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class MaterialDomainModel(
    val moduleId: String,
    val title: String,
    val content: String,
    val isRead: Boolean = false
)
