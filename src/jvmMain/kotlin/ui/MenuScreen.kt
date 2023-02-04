package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import menuVisible
import speed

private val fontFamily = FontFamily(
    Font(
        resource = "Ubuntu-Medium.ttf",
        weight = FontWeight.W400,
        style = FontStyle.Normal
    )
)

@Composable
fun menuScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                "Snake Game",
                style = TextStyle(fontFamily = fontFamily, fontSize = 24.sp, color = Color.Blue),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
            )
            Button(onClick = {
                menuVisible.value = false
                speed.value = 250
            }, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 40.dp)) { Text("Easy") }
            Button(onClick = {
                menuVisible.value = false
                speed.value = 125
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Medium") }
            Button(onClick = {
                menuVisible.value = false
                speed.value = 50
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Hard") }
        }
    }
}