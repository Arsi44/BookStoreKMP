package com.analystlab.app.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.analystlab.app.domain.model.ModuleDomainModel
import com.analystlab.app.presentation.dashboard.components.ModuleCard
import com.analystlab.app.presentation.dashboard.components.StatsRow
import com.analystlab.app.theme.BackgroundGray
import com.analystlab.app.theme.PrimaryBlue
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navigator: Navigator,
    viewModel: DashboardViewModel = koinInject(),
    onModuleClick: (ModuleDomainModel) -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AnalystLab",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Text(
                            text = "Портал обучения",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else if (state.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "⚠️",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Не удалось загрузить данные",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = state.error ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Приветствие
                item {
                    Text(
                        text = "Добро пожаловать в AnalystLab! 👋",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Моя статистика",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Статистика (логика как в десктопе):
                // — Всего модулей: число модулей в курсе
                // — Завершено: модули, у которых ВСЕ активности отмечены выполненными
                // — В процессе: модули, у которых ЕСТЬ хотя бы одна выполненная активность, но не все
                item {
                    StatsRow(
                        progressPercent = state.stats.progressPercent,
                        totalModules = state.modules.size,
                        completedModules = state.modules.count { it.completed },
                        inProgressModules = state.modules.count { m ->
                            !m.completed && m.activities.any { it.completed }
                        }
                    )
                }
                
                // Текущий модуль
                if (state.currentModule != null) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Текущий модуль",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    item {
                        ModuleCard(
                            module = state.currentModule!!,
                            isSelected = true,
                            onClick = { onModuleClick(state.currentModule!!) }
                        )
                    }
                }
            }
        }
    }
}
