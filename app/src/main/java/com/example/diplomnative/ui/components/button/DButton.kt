package com.example.diplomnative.ui.components.button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.diplomnative.ui.theme.DiplomNativeTheme

@Composable
fun DButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun DButtonPreview() {
    DiplomNativeTheme {
        DButton("Example", onClick = {})
    }
}