package com.analystlab.app.presentation.material

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalUriHandler
import com.seiko.imageloader.rememberImagePainter
import com.analystlab.app.theme.BackgroundGray
import com.analystlab.app.theme.PrimaryBlue
import com.analystlab.app.theme.SuccessGreen
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("UNUSED_PARAMETER")
fun MaterialScreen(
    navigator: Navigator,
    moduleId: String,
    viewModel: MaterialViewModel = koinInject(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(moduleId) {
        viewModel.loadMaterial(moduleId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Материал") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    if (state.isMarkedAsRead) {
                        Row(
                            modifier = Modifier.padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Прочитано",
                                color = SuccessGreen,
                                fontSize = 12.sp
                            )
                        }
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
                Text(
                    text = "Ошибка: ${state.error}",
                    color = Color.Red
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Header
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = state.module?.name ?: "",
                                style = MaterialTheme.typography.titleSmall,
                                color = PrimaryBlue
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Материал",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Content
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            // Заглушка контента материала
                            MaterialContent(moduleId = moduleId, moduleName = state.module?.name ?: "")
                        }
                    }
                }
                
                // Bottom action - адаптивная панель
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Кнопка "Отметить как прочитано" - основное действие
                        if (state.isMarkedAsRead) {
                            Button(
                                onClick = {},
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.LightGray,
                                    contentColor = Color.Gray
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(vertical = 14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Уже прочитано")
                            }
                        } else {
                            Button(
                                onClick = { viewModel.onEvent(MaterialEvent.MarkAsRead) },
                                enabled = !state.isMarkingAsRead,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SuccessGreen
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(vertical = 14.dp)
                            ) {
                                if (state.isMarkingAsRead) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Отметить как прочитано")
                            }
                        }
                        
                        // Кнопка "Назад" - вторичное действие
                        Button(
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Gray
                            ),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Назад к модулю")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MaterialContent(moduleId: String, moduleName: String) {
    when (moduleId) {
        "module-1" -> Module1LinuxMaterialContent()
        else -> DefaultMaterialContent(moduleName = moduleName)
    }
}

@Composable
private fun DefaultMaterialContent(moduleName: String) {
    Column {
        Text(
            text = "Введение",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Добро пожаловать в модуль \"$moduleName\". В этом разделе вы изучите основные концепции и принципы работы.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Основные понятия",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        BulletPoint("Изучите теоретические основы темы")
        BulletPoint("Рассмотрите практические примеры применения")
        BulletPoint("Выполните упражнения для закрепления материала")

        Spacer(modifier = Modifier.height(24.dp))

        InfoBox(
            text = "Важно: После изучения материала не забудьте пройти тест для проверки знаний."
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Заключение",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "После завершения изучения материала отметьте его как прочитанный и переходите к следующим активностям модуля.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )
    }
}

/**
 * Контент модуля 1, перенесённый из десктопного шаблона `templates/modules/module-1.html`.
 * Разделы: загрузка Linux, системные вызовы, жизненный цикл процессов, сигналы, термины, доп.материалы.
 */
@Composable
private fun Module1LinuxMaterialContent() {
    val uriHandler = LocalUriHandler.current
    val backendBaseUrl = "http://10.0.2.2:8000"

    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        // Заголовок (как в десктопе)
        Text(
            text = "Теоретические Основы Linux",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        BodyText("Этапы загрузки, сигналы, жизненный цикл процессов, OOM Killer, системные вызовы")
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "2 часа", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text(text = "3 активности", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }

        // ========== Этапы загрузки Linux ==========
        SectionTitle("Этапы загрузки Linux")

        SubTitle("1) Запуск BIOS")
        BodyText("Запуск BIOS - аппаратный этап процесса загрузки. Запускается самотестирование POST (Power On Self Testing). Проверяется базовая работоспособность оборудования. ")

        SubTitle("2) Поиск MBR (Master Boot Record)")
        BodyText("При успешном завершении тестов BIOS производит поиск MBR (Master Boot Record) на всех подключенных загрузочных устройствах (Жёсткие Диски, USB и др.). Первый обнаруженный загрузочный сектор, содержащий загрузочную запись, загружается в ОЗУ (оперативную память). Далее управление передаётся загрузчику, полученному из загрузочного сектора. Загрузчик называется GRUB 2.")

        SubTitle("3) GRUB 2 находит ядро операционной системы ")
        BodyText(
            """
GRUB 2 находит ядро операционной системы и загружает его в оперативную память и далее передаёт управление компьютером ядру.
    Grub обнаруживает другие MBR, если они есть
    Позволяет выбрать версию ядра операционной системы. 
    Пояснение: Каждый раз, когда вы обновляете ядро Linux, старое ядро обычно не удаляется автоматически. Вместо этого новое ядро добавляется в список загрузочных опций в меню GRUB2.
    Настройки grub можно менять по следующему пути /boot/grab/grab.cfg
            """.trimIndent()
        )

        SubTitle("4) Ядро ОС (Kernel) начинает инициализацию оборудования")
        BodyText(
            """
Ядро операционной системы (Kernel) начинает инициализацию оборудования и запускает процесс, который называется init (PID 1). После того, как ядро загрузилось полностью, оно передаёт управление менеджеру системы.
    В современных дистрибутивах часто используется systemd как система инициализации.
    Развёрнутое описание: Инициализируются все подключенные устройства - процессоры, память, диски и сетевые интерфейсы, загружаются драйверы для работы этих устройств. После инициализации оборудования ядро запускает первый процесс в пользовательском пространстве. Этот процесс традиционно называется init и имеет PID 1(идентификатор процесса). В современных системах процесс init часто заменён на systemd, который выполняет те же функции, но с более широкими возможностями. 
    Операционная система пока что не смонтирована на момент инициализации, поэтому для развёртывания используется временная файловая система (файл initrd.img)
    Все ядра находятся в каталоге /boot
    PID можно посмотреть командой top 
            """.trimIndent()
        )

        SubTitle("5) Systemd — основной родитель для всех процессов")
        BodyText(" Systemd является основным родителем для всех процессов в системе и отвечает за доведение хоста Linux до состояния, в котором можно выполнять работу. Systemd монтирует файловые системы, как определено в файле /etc/fstab, включая любые файлы подкачки или разделы. На этом этапе он уже может получить доступ к файлам конфигурации, расположенным в /etc, включая свои конфигурационные файлы.")
        BodyText("Примеры: монтирует ФС по /etc/fstab (в т.ч. swap), обращается к конфигурации в /etc.")

        DesktopNoteBlock(
            boldPrefix = "Обратите внимание.",
            text = "Выше дан типовой наиболее частый алгоритм загрузки Linux. Компоненты на некоторых этапах могут отличаться на том или ином железе."
        )

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        // ========== Системные вызовы и зомби в Linux ==========
        SectionTitle("Системные вызовы и зомби в Linux")
        BodyText("fork() - системный вызов, при котором процессы клонируются. Появляется 2 одинаковых процесса, с общей памятью, и если новый процесс пытается что-то записать, то ему будет выделен отдельный кусочек памяти.")
        BodyText("exec() - системный вызов, при котором процессы заменяются на другие с сохранением id.")
        BodyText("clone() - системный вызов, который позволяет клонировать объект со всеми его ресурсами.")
        BodyText("Zombie - дочерний процесс, запись о котором не была удалена, и след остался у родительского процесса. (Когда порождённый процесс отработал, он убивается и отправляет сигнал signal, а запись об этом процессе исчезает, но при возникновении ошибки у родителя этого процесса сбоев, запись дочернего процесса не удаляется. То есть процесс не существует, а запись о нём есть)")

        SubTitle("Процессы и потоки в Linux:")
        BodyText("Процесс - это экземпляр запущенной программы, который содержит код программы и ее текущее состояние. Процесс имеет уникальный идентификатор, называемый PID (Process ID).")
        BodyText("Поток - это наименьшая единица обработки, которую может управлять операционная система.")
        BodyText("Потоки внутри одного процесса могут делиться некоторыми ресурсами, такими как память, что делает обмен данными между потоками быстрее, чем между процессами.")
        BodyText("Потоки реализованы как обычные процессы. Однако, потоки одного процесса имеют общее пространство памяти и другие ресурсы. Это позволяет потокам эффективно обмениваться информацией и синхронизировать свою работу.")

        SubTitle("Out-Of-Memory Killer LInux")
        BodyText("В Linux существует механизм, известный как \"OOM killer\" (Out-Of-Memory Killer), который активируется, когда система находится в критическом состоянии нехватки памяти. OOM Killer в Linux выбирает процесс для завершения на основе различных эвристик, таких как оценка \"badness score\", которая учитывает, насколько процесс превышает разрешённое количество памяти. Цель OOM killer - сохранить работоспособность системы, завершая процессы, которые потребляют больше всего памяти и считаются наименее важными для системы.")

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        // ========== Жизненный цикл процессов ==========
        SectionTitle("Жизненный цикл процессов")
        BodyText("Процесс — это экземпляр выполняющейся программы, содержащий её код и текущее состояние. Имеет уникальный идентификатор PID (Process ID).")
        BodyText("Поток — Наименьшая единица выполнения, которой может управлять ОС. Потоки одного процесса могут разделять память, что ускоряет обмен данными. Потоки реализованы как обычные процессы, но с общим адресным пространством и ресурсами.")
        DesktopDetailsImageToggle(imageUrl = "$backendBaseUrl/static/materials/process_lifecycle.png")

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        // ========== Сигналы в Linux ==========
        SectionTitle("Сигналы в Linux")
        BodyText("Сигналы — это механизм в Unix/Linux для асинхронного взаимодействия между процессами и ОС.")
        BodyText("Сигнал — это уведомление процессу о каком-либо событии (например, завершение, ошибка, остановка и т.д.).")

        SubTitle("Как процессы реагируют на сигналы?")
        BodyText("Процесс может:")
        BulletPoint("Обработать сигнал с помощью обработчика (signal handler)")
        BulletPoint("Игнорировать сигнал (не все)")
        BulletPoint("Принять действие по умолчанию (обычно — завершение процесса)")

        SignalsTable()

        SubTitle("Команды для работы с сигналами:")

        CodeBlock(
            """
            # Отправить сигнал процессу:
            kill -SIGNAL PID
            kill -9 1234        # SIGKILL — принудительно завершить
            kill -SIGTERM 5678  # мягкое завершение

            # Посмотреть список сигналов:
            kill -l

            # Отправить пользовательский сигнал:
            kill -USR1 1234

            # Сигнал всем процессам по имени:
            pkill -SIGTERM nginx

            # Использовать trap в bash-скрипте для обработки:
            trap "echo 'Процесс прерван'; exit" SIGINT
            """.trimIndent()
        )

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        // ========== Термины ==========
        SectionTitle("Термины")
        Term(
            term = "BIOS (Basic Input/Output System)",
            definition = "— это базовая система ввода-вывода, которая является важным компонентом компьютера. BIOS — это прошивка, встроенная в материнскую плату, которая выполняет первичную инициализацию оборудования при включении компьютера и предоставляет базовые функции для взаимодействия с аппаратным устройствами."
        )
        Term(
            term = "MBR (Master Boot Record)",
            definition = "- это специальный сектор на диске (обычно первый сектор), который содержит информацию о загрузочной записи и таблицу разделов."
        )
        Term(
            term = "GRUB2 (GRand Unified Bootloader, version 2)",
            definition = "— это загрузчик, который используется для загрузки операционной системы на компьютере. GRUB2 загружается первым после BIOS/UEFI и предоставляет меню выбора операционной системы для загрузки."
        )
        Term(
            term = "Kernel (Ядро)",
            definition = "- это центральная часть операционной системы, которая управляет аппаратными ресурсами и обеспечивает взаимодействие между аппаратурой и ПО."
        )
        Term(
            term = "Systemd",
            definition = "- это система инициализации и менеджер системы, используемый в большинстве современных дистрибутивов Linux. Он управляет службами и процессами, которые запускаются при старте системы, а также предоставляет разнообразные функции для управления услугами во время работы."
        )
        Term(
            term = "Файлы подкачки",
            definition = "- это специальные файлы на диске, которые используются операционной системой Linux для временного хранения данных из оперативной памяти (ОЗУ), когда её становится недостаточно для выполнения всех текущих процессов."
        )

        Divider(modifier = Modifier.padding(vertical = 4.dp))

        // ========== Дополнительные материалы ==========
        SectionTitle("Дополнительные материалы")
        SubTitle("1. Загрузка Linux ")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { uriHandler.openUri("https://habr.com/ru/articles/714986/") }
                .background(PrimaryBlue.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Перейти к статье", color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
        }

        SubTitle("2. Схема процесса загрузки Linux")
        DesktopDetailsImageToggle(imageUrl = "$backendBaseUrl/static/materials/linux_boot_flowchart.png")
    }
}

@Composable
private fun DesktopNoteBlock(boldPrefix: String, text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = PrimaryBlue.copy(alpha = 0.10f), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = boldPrefix, color = PrimaryBlue, fontWeight = FontWeight.Bold)
            Text(text = text, color = Color(0xFF1E3A8A), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun DesktopDetailsImageToggle(imageUrl: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (expanded) "Скрыть" else "Развернуть",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF374151),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = Color(0xFF6B7280)
            )
        }

        if (expanded) {
            Divider(color = Color(0xFFE5E7EB))
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .padding(6.dp)
            )
        }
    }
}

@Composable
private fun SignalsTable() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SignalsTableRow(
                signal = "Сигнал",
                number = "Номер",
                purpose = "Назначение",
                defaultAction = "Действие по умолчанию",
                isHeader = true
            )
            Divider(color = Color(0xFFE5E7EB))

            SignalsTableRow("SIGHUP", "1", "Завершение при закрытии терминала", "Завершить процесс")
            SignalsTableRow("SIGINT", "2", "Прерывание (Ctrl+C)", "Завершить процесс")
            SignalsTableRow("SIGQUIT", "3", "Прерывание с дампом памяти (Ctrl+\\)", "Завершить + core dump")
            SignalsTableRow("SIGKILL", "9", "Безусловное завершение", "Завершить (необрабатываемый)")
            SignalsTableRow("SIGTERM", "15", "Корректное завершение", "Завершить процесс")
            SignalsTableRow("SIGSTOP", "19", "Остановка процесса (Ctrl+Z)", "Приостановить")
            SignalsTableRow("SIGCONT", "18", "Продолжить после SIGSTOP", "Продолжить исполнение")
            SignalsTableRow("SIGALRM", "14", "Таймер (alarm)", "Завершить процесс")
            SignalsTableRow("SIGUSR1/2", "10/12", "Пользовательские сигналы", "По умолчанию — завершение")
        }
    }
}

@Composable
private fun SignalsTableRow(
    signal: String,
    number: String,
    purpose: String,
    defaultAction: String,
    isHeader: Boolean = false
) {
    val bg = if (isHeader) Color(0xFFF3F4F6) else Color.White
    val weight = if (isHeader) FontWeight.SemiBold else FontWeight.Normal
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = signal,
            modifier = Modifier.weight(0.9f),
            fontFamily = if (isHeader) null else FontFamily.Monospace,
            fontWeight = weight,
            fontSize = 12.sp
        )
        Text(text = number, modifier = Modifier.weight(0.5f), fontWeight = weight, fontSize = 12.sp)
        Text(text = purpose, modifier = Modifier.weight(1.7f), fontWeight = weight, fontSize = 12.sp)
        Text(text = defaultAction, modifier = Modifier.weight(1.7f), fontWeight = weight, fontSize = 12.sp)
    }
    if (!isHeader) Divider(color = Color(0xFFF3F4F6))
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
}

@Composable
private fun SubTitle(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = Color.Black)
}

@Composable
private fun BodyText(text: String) {
    Text(text = text, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
}

@Composable
private fun InfoBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = PrimaryBlue.copy(alpha = 0.10f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Row {
            Text(text = "💡", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryBlue
            )
        }
    }
}

@Composable
private fun CodeBlock(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF111827), shape = RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Text(
            text = code,
            color = Color(0xFFE5E7EB),
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun Term(term: String, definition: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = term, fontWeight = FontWeight.SemiBold, color = Color.Black)
        Text(text = definition, color = Color.DarkGray, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun CollapsibleImage(title: String, imageUrl: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF374151),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = Color(0xFF6B7280)
            )
        }

        if (expanded) {
            Divider(color = Color(0xFFE5E7EB))
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .padding(6.dp)
            )
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyMedium,
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )
    }
}
