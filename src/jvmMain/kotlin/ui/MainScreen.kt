package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import table

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainScreen(move : (input: String) -> Unit) {
    MaterialTheme {
        val requester = remember { FocusRequester() }
        Box(modifier = Modifier.onKeyEvent { event ->
            when (event.key) {
                Key.W -> move("w")
                Key.A -> move("a")
                Key.S -> move("s")
                Key.D -> move("d")
            }
            true
        }.focusRequester(requester).focusable()) {
            LazyVerticalGrid(GridCells.Fixed(table.size)) {
                items(table.size * table.size) {
                    SqPxItem(table[it / table.size][it % table.size].value.toString())
                }
            }
        }
        LaunchedEffect(Unit) {
            requester.requestFocus()
        }
    }
}

@Composable
fun SqPxItem(text: String) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .background(Color.Blue)
    ) {
        Text(text, Modifier.align(Alignment.Center), Color.White, fontSize = 16.sp)
    }
}