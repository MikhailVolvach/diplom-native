package com.example.diplomnative

import android.os.Bundle
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.diplomnative.ui.screens.HistoryScreen
import com.example.diplomnative.ui.screens.HomeScreen
import com.example.diplomnative.ui.screens.TransferScreen
import com.example.diplomnative.ui.theme.BankScreenBackground
import com.example.diplomnative.ui.theme.DiplomNativeTheme
import com.example.diplomnative.ui.widgets.GreetingSection

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiplomNativeTheme {
                DiplomNativeApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun DiplomNativeApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GreetingSection()
        },
        containerColor = BankScreenBackground,
    ) { innerPadding ->
        NavigationSuiteScaffold(
            modifier = Modifier.fillMaxSize().padding(innerPadding),

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
            containerColor = Color(0x00FFFFFF),
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationBarContainerColor = Color(0x00FFFFFF),
            ),
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(24.dp),
                contentColor = MaterialTheme.colorScheme.background
            ) {
                when (currentDestination) {
                    AppDestinations.HOME -> HomeScreen()
                    AppDestinations.TRANSFER -> TransferScreen()
                    AppDestinations.HISTORY -> HistoryScreen()
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