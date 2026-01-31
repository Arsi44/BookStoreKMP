package com.analystlab.app.presentation.material

import com.analystlab.app.domain.model.ModuleDomainModel

data class MaterialState(
    val isLoading: Boolean = true,
    val module: ModuleDomainModel? = null,
    val isMarkedAsRead: Boolean = false,
    val isMarkingAsRead: Boolean = false,
    val error: String? = null
)

sealed class MaterialEvent {
    data class LoadMaterial(val moduleId: String) : MaterialEvent()
    data object MarkAsRead : MaterialEvent()
}
