package com.analystlab.app.presentation.module

import com.analystlab.app.domain.repository.ModulesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class ModuleViewModel(
    private val modulesRepository: ModulesRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(ModuleState())
    val state: StateFlow<ModuleState> = _state.asStateFlow()
    
    fun onEvent(event: ModuleEvent) {
        when (event) {
            is ModuleEvent.LoadModule -> loadModule(event.moduleId)
            is ModuleEvent.OpenActivity -> {
                // Handled in UI
            }
        }
    }
    
    fun loadModule(moduleId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            modulesRepository.getModule(moduleId)
                .onSuccess { module ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            module = module
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
}
