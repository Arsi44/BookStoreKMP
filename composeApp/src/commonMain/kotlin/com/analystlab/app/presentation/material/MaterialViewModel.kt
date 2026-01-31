package com.analystlab.app.presentation.material

import com.analystlab.app.domain.repository.ModulesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MaterialViewModel(
    private val modulesRepository: ModulesRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(MaterialState())
    val state: StateFlow<MaterialState> = _state.asStateFlow()
    
    private var currentModuleId: String = ""
    
    fun onEvent(event: MaterialEvent) {
        when (event) {
            is MaterialEvent.LoadMaterial -> loadMaterial(event.moduleId)
            is MaterialEvent.MarkAsRead -> markAsRead()
        }
    }
    
    fun loadMaterial(moduleId: String) {
        currentModuleId = moduleId
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            modulesRepository.getModule(moduleId)
                .onSuccess { module ->
                    // Проверяем, прочитан ли уже материал
                    val readingActivity = module.activities.find { it.type == "reading" }
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            module = module,
                            isMarkedAsRead = readingActivity?.completed == true
                        ) 
                    }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = error.message
                        ) 
                    }
                }
        }
    }
    
    private fun markAsRead() {
        if (currentModuleId.isEmpty()) return
        
        viewModelScope.launch {
            _state.update { it.copy(isMarkingAsRead = true) }
            
            modulesRepository.completeActivity(currentModuleId, "reading")
                .onSuccess {
                    _state.update { 
                        it.copy(
                            isMarkingAsRead = false,
                            isMarkedAsRead = true
                        ) 
                    }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(
                            isMarkingAsRead = false,
                            error = "Не удалось отметить как прочитано"
                        ) 
                    }
                }
        }
    }
}
