package com.analystlab.app.presentation.module

import com.analystlab.app.domain.model.ActivityDomainModel
import com.analystlab.app.domain.model.ModuleDomainModel

data class ModuleState(
    val isLoading: Boolean = true,
    val module: ModuleDomainModel? = null,
    val error: String? = null
)

sealed class ModuleEvent {
    data class LoadModule(val moduleId: String) : ModuleEvent()
    data class OpenActivity(val activity: ActivityDomainModel) : ModuleEvent()
}
