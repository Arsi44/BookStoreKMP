package com.analystlab.app.presentation.modules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.analystlab.app.domain.model.ActivityDomainModel
import com.analystlab.app.domain.model.ModuleDomainModel
import com.analystlab.app.presentation.dashboard.DashboardViewModel
import com.analystlab.app.theme.BackgroundGray
import com.analystlab.app.theme.PrimaryBlue
import com.analystlab.app.theme.SuccessGreen
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModulesScreen(
    navigator: Navigator,
    viewModel: DashboardViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()

    // По UX как на скрине — разворачиваем максимум один модуль за раз
    var expandedModuleId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Модули") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ошибка: ${state.error}",
                        color = Color.Red
                    )
                }
            }

            else -> {
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        ModulesHeaderCard()
                    }

                    items(state.modules.size) { index ->
                        val module = state.modules[index]
                        val isExpanded = expandedModuleId == module.id

                        ModuleAccordionCard(
                            module = module,
                            expanded = isExpanded,
                            onToggle = {
                                expandedModuleId = if (isExpanded) null else module.id
                            },
                            onActivityClick = { activityType ->
                                when (activityType) {
                                    "reading" -> navigator.navigate("/material/${module.id}")
                                    "test" -> navigator.navigate("/test/${module.id}")
                                    "oral" -> navigator.navigate("/oral/${module.id}")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModulesHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color = PrimaryBlue.copy(alpha = 0.10f), shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MenuBook,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Модули курса",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
private fun ModuleAccordionCard(
    module: ModuleDomainModel,
    expanded: Boolean,
    onToggle: () -> Unit,
    onActivityClick: (String) -> Unit
) {
    val leftStatusIcon = if (module.completed) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked
    val leftStatusTint = if (module.completed) SuccessGreen else Color(0xFF9CA3AF)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // Заголовок модуля
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = leftStatusIcon,
                    contentDescription = null,
                    tint = leftStatusTint
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = module.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = Color(0xFF6B7280)
            )
        }

        if (expanded) {
            Divider(color = Color(0xFFF3F4F6))
            Column(modifier = Modifier.fillMaxWidth()) {
                ActivityRow(
                    title = "Материал",
                    icon = Icons.Outlined.MenuBook,
                    completed = module.findActivity("reading")?.completed == true,
                    onClick = { onActivityClick("reading") }
                )
                Divider(color = Color(0xFFF3F4F6))
                ActivityRow(
                    title = "Тест",
                    icon = Icons.Outlined.Quiz,
                    completed = module.findActivity("test")?.completed == true,
                    onClick = { onActivityClick("test") }
                )
                Divider(color = Color(0xFFF3F4F6))
                ActivityRow(
                    title = "Устный ответ",
                    icon = Icons.Outlined.Mic,
                    completed = module.findActivity("oral")?.completed == true,
                    onClick = { onActivityClick("oral") }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun ActivityRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    completed: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF6B7280)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF374151)
            )
        }

        if (completed) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private fun ModuleDomainModel.findActivity(type: String): ActivityDomainModel? =
    activities.firstOrNull { it.type == type }

