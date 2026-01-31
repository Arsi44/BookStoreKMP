package com.analystlab.app.data.remote

import com.analystlab.app.domain.model.ModuleDomainModel
import com.analystlab.app.domain.model.StatsDomainModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.serialization.Serializable

class ModulesApi(private val httpClient: HttpClient) {
    
    suspend fun getModules(): List<ModuleDomainModel> {
        return httpClient.get("/api/modules").body()
    }
    
    suspend fun getModule(moduleId: String): ModuleDomainModel {
        return httpClient.get("/api/modules/$moduleId").body()
    }
    
    suspend fun getStats(token: String): StatsDomainModel {
        return httpClient.get("/api/course/progress/stats") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }
    
    suspend fun completeActivity(
        token: String,
        moduleId: String,
        activityType: String
    ): CompleteActivityResponse {
        return httpClient.post("/api/course/progress/complete") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(CompleteActivityRequest(moduleId, activityType))
        }.body()
    }
}

@Serializable
data class CompleteActivityRequest(
    val module_id: String,
    val activity_type: String
)

@Serializable
data class CompleteActivityResponse(
    val completed_activities: Map<String, Map<String, Boolean>> = emptyMap()
)
