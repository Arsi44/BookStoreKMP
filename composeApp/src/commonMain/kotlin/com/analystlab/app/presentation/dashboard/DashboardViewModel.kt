package com.analystlab.app.presentation.dashboard

import com.analystlab.app.domain.repository.ModulesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class DashboardViewModel(
    private val modulesRepository: ModulesRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()
    
    init {
        loadData()
    }
    
    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.LoadData -> loadData()
            is DashboardEvent.Refresh -> loadData()
            is DashboardEvent.SelectModule -> {
                _state.update { it.copy(currentModule = event.module) }
            }
        }
    }
    
    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            // Загружаем модули
            modulesRepository.getModules()
                .onSuccess { modules ->
                    val currentModule = modules.firstOrNull { !it.completed } ?: modules.firstOrNull()
                    _state.update { 
                        it.copy(
                            modules = modules,
                            currentModule = currentModule
                        ) 
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(error = error.message) }
                }
            
            // Загружаем статистику
            modulesRepository.getStats()
                .onSuccess { stats ->
                    _state.update { it.copy(stats = stats) }
                }
            
            _state.update { it.copy(isLoading = false) }
        }
    }
}
