package com.analystlab.app.presentation.dashboard

import com.analystlab.app.domain.model.ModuleDomainModel
import com.analystlab.app.domain.model.StatsDomainModel

data class DashboardState(
    val isLoading: Boolean = true,
    val stats: StatsDomainModel = StatsDomainModel(),
    val modules: List<ModuleDomainModel> = emptyList(),
    val currentModule: ModuleDomainModel? = null,
    val error: String? = null
)

sealed class DashboardEvent {
    data object LoadData : DashboardEvent()
    data object Refresh : DashboardEvent()
    data class SelectModule(val module: ModuleDomainModel) : DashboardEvent()
}
