package com.example.diplomnative

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diplomnative.data.BankCardEntity
import com.example.diplomnative.ui.screens.HistoryScreen
import com.example.diplomnative.ui.screens.HomeScreen
import com.example.diplomnative.ui.screens.NotificationScreen
import com.example.diplomnative.ui.screens.TransferScreen
import com.example.diplomnative.ui.theme.*
import com.example.diplomnative.ui.viewmodel.BankViewModel
import com.example.diplomnative.ui.widgets.GreetingSection
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val app = context.applicationContext as BankApplication
            
            val bankViewModel: BankViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BankViewModel(app.repository) as T
                    }
                }
            )

            // Подписка на тосты из ViewModel
            LaunchedEffect(Unit) {
                bankViewModel.toastEvent.collectLatest { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }

            // Инициализация данных при первом запуске
            val cards by bankViewModel.allCards.collectAsState()
            LaunchedEffect(cards) {
                if (cards.isEmpty()) {
                    bankViewModel.initMockData(
                        listOf(
                            BankCardEntity(name = "Зарплатная", balance = 45200.0, colorHex = CardRuby.toArgb().toLong()),
                            BankCardEntity(name = "Сберегательный", balance = 150000.5, colorHex = CardSapphire.toArgb().toLong()),
                            BankCardEntity(name = "Кредитная", balance = 10000.0, colorHex = CardGraphite.toArgb().toLong())
                        )
                    )
                }
            }

            DiplomNativeTheme {
                DiplomNativeApp(bankViewModel)
            }
        }
    }
}

@Composable
fun DiplomNativeApp(viewModel: BankViewModel) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var showNotifications by rememberSaveable { mutableStateOf(false) }

    if (showNotifications) {
        NotificationScreen(viewModel = viewModel, onBack = { showNotifications = false })
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                GreetingSection(onNotificationsClick = {showNotifications = true})
            },
            containerColor = BankScreenBackground,
        ) { innerPadding ->
            NavigationSuiteScaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                navigationSuiteItems = {
                    AppDestinations.entries.forEach {
                        item(
                            icon = { Icon(it.icon, contentDescription = it.label) },
                            label = { Text(it.label) },
                            selected = it == currentDestination,
                            onClick = { currentDestination = it }
                        )
                    }
                },
                containerColor = Color.Transparent,
                navigationSuiteColors = NavigationSuiteDefaults.colors(
                    navigationBarContainerColor = Color.Transparent,
                ),
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentDestination) {
                        AppDestinations.HOME -> HomeScreen(viewModel = viewModel)
                        AppDestinations.TRANSFER -> TransferScreen(viewModel = viewModel)
                        AppDestinations.HISTORY -> HistoryScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String = "",
    val icon: ImageVector,
) {
    HOME("Главная", Icons.Default.Home),
    TRANSFER("Перевод", Icons.AutoMirrored.Default.Send),
    HISTORY("История", Icons.AutoMirrored.Filled.List),
}
