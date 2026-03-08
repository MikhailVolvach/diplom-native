package com.example.diplomnative.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomnative.ui.components.button.DButton

@Preview(showBackground = true)
@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        androidx.compose.foundation.layout.Column(
            modifier = modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Top,
        ) {
            Text("History screen", modifier = Modifier.size(100.dp))
            DButton("Кнопка", onClick = {})
        }
    }
}