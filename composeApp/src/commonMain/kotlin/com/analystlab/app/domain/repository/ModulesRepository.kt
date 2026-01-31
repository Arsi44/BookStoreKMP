package com.analystlab.app.domain.repository

import com.analystlab.app.domain.model.ModuleDomainModel
import com.analystlab.app.domain.model.StatsDomainModel

interface ModulesRepository {
    suspend fun getModules(): Result<List<ModuleDomainModel>>
    suspend fun getModule(moduleId: String): Result<ModuleDomainModel>
    suspend fun getStats(): Result<StatsDomainModel>
    suspend fun completeActivity(moduleId: String, activityType: String): Result<Unit>
}
