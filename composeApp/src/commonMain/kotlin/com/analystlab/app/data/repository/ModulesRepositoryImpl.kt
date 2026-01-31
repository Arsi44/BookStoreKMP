package com.analystlab.app.data.repository

import com.analystlab.app.data.local.TokenStorage
import com.analystlab.app.data.remote.ModulesApi
import com.analystlab.app.domain.model.ModuleDomainModel
import com.analystlab.app.domain.model.StatsDomainModel
import com.analystlab.app.domain.repository.ModulesRepository

class ModulesRepositoryImpl(
    private val modulesApi: ModulesApi,
    private val tokenStorage: TokenStorage
) : ModulesRepository {
    
    override suspend fun getModules(): Result<List<ModuleDomainModel>> {
        return try {
            val modules = modulesApi.getModules()
            Result.success(modules)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getModule(moduleId: String): Result<ModuleDomainModel> {
        return try {
            val module = modulesApi.getModule(moduleId)
            Result.success(module)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getStats(): Result<StatsDomainModel> {
        return try {
            val token = tokenStorage.accessToken
                ?: return Result.failure(Exception("Not authenticated"))
            val stats = modulesApi.getStats(token)
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun completeActivity(moduleId: String, activityType: String): Result<Unit> {
        return try {
            val token = tokenStorage.accessToken
                ?: return Result.failure(Exception("Not authenticated"))
            modulesApi.completeActivity(token, moduleId, activityType)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
