package ui

import alertDialogMessage
import alertDialogVisible
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
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
import androidx.compose.ui.window.*
import table

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Preview
@Composable
fun mainScreen(scope: ApplicationScope, move: (input: String) -> Unit) {
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
                    sqPxItem(table[it / table.size][it % table.size].value)
                }
            }
        }
        LaunchedEffect(Unit) {
            requester.requestFocus()
        }
        showAlertDialog(scope)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun showAlertDialog(scope: ApplicationScope) {
    val windowState = rememberWindowState(width = 300.dp, height = 200.dp, position = WindowPosition(Alignment.Center))
    Window(
        onCloseRequest = {alertDialogVisible.value = false},
        title = "Game is Over",
        visible = alertDialogVisible.value,
        state = windowState
    ) {
        AlertDialog(
            modifier = Modifier.width(300.dp).height(200.dp),
            onDismissRequest = {
                alertDialogVisible.value = false
            },
            title = { Text("Game is Over") },
            confirmButton = {
                Row {
                    Button(onClick = {
                        alertDialogVisible.value = false
                        scope.exitApplication()
                    }) {
                        Text("Close Game")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        alertDialogVisible.value = false
                    }) {
                        Text("Try Again")
                    }
                }
            },
            text = { Text(alertDialogMessage.value) })
    }
}

@Composable
fun sqPxItem(color: Color) {
    Box(
        modifier = Modifier
            .background(color).width(4.dp).height(20.dp)
    )
}