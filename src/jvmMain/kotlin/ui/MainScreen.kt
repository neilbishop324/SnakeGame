package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import table

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun MainScreen() {
    var i = 0
    MaterialTheme {
        LazyVerticalGrid(GridCells.Fixed(table.size)) {
            items(table.size * table.size) {
                SqPxItem(table[i / table.size][i % table.size].value.toString())
                i++
            }
        }
    }

}

@Composable
fun SqPxItem(text: String) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(Color.Blue)
    ) {
        Text(text, Modifier.align(Alignment.Center), Color.White, fontSize = 14.sp)
    }
}