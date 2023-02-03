import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import sealed.Dest
import sealed.Target
import ui.MainScreen
import java.util.*
import kotlin.collections.ArrayList

var table_size = 30
var table = Array(table_size) { Array(table_size) { _ -> mutableStateOf(Color.Black) } }

var snake_y = table_size / 2
var snake = arrayListOf(
    arrayListOf(snake_y, 7),
    arrayListOf(snake_y, 6),
    arrayListOf(snake_y, 5),
    arrayListOf(snake_y, 4),
    arrayListOf(snake_y, 3),
    arrayListOf(snake_y, 2)
)
var snake_dest: Dest = Dest.D

var isGameStarted = false

fun main() {
    Game().openGame()
}

private val score = mutableStateOf(0)
val alertDialogVisible = mutableStateOf(false)
val alertDialogMessage = mutableStateOf("")

class Game {

    private val snakeHead = Color.Blue
    private val snakeBody = Color.Green
    private val appleChar = Color.Red
    private val tableBgChar = Color.Black
    private val wallChar = Color(0xFF310018)

    private var isTaskStarted = false

    fun openGame() {
        drawApple()
        startGame()
    }

    private fun startGame() = application {
        val gameWindowState = rememberWindowState(width = 600.dp, height = 633.dp, position = WindowPosition(Alignment.Center))
        Window(
            onCloseRequest = ::exitApplication,
            title = "Snake Game, Score: ${score.value}",
            state = gameWindowState
        ) {
            MainScreen(this@application) {
                moveControl(it)
            }
            drawSnake()
            drawWall()
        }
    }

    private fun drawWall() {
        for (y in 0 until table_size) {
            for (x in 0 until table_size) {
                if (y == 0 || y == table_size - 1 || x == 0 || x == table_size - 1) {
                    table[y][x].value = wallChar
                }
            }
        }
    }

    //2
    private fun drawSnake() {
        for (i in snake.indices) {
            table[snake[i][0]][snake[i][1]].value = if (i == 0) snakeHead else snakeBody
        }
    }

    //3
    private fun drawApple() {
        val emptyList = ArrayList<ArrayList<Int>>()
        for (y in table.indices) {
            table[y].forEach {
                if (it.value == appleChar) {
                    return
                }
            }
            for (x in table[y].indices) {
                if (table[y][x].value == tableBgChar &&
                    !snake.contains(arrayListOf(y, x)) &&
                    (y != 0 && y != table_size - 1 && x != 0 && x != table_size - 1)
                ) {
                    emptyList.add(arrayListOf(y, x))
                }
            }
        }
        val apple = emptyList.random()
        table[apple[0]][apple[1]].value = appleChar
    }

    //5
    private fun moveControl(destination: String?) {
        when (destination?.lowercase()) {
            "w" -> snake_dest = Dest.W
            "a" -> snake_dest = Dest.A
            "s" -> snake_dest = Dest.S
            "d" -> snake_dest = Dest.D
        }

        isGameStarted = true
        alertDialogVisible.value = false

        if (!isTaskStarted) {
            timer.scheduleAtFixedRate(task, 0, 125)
            isTaskStarted = true
        }
    }

    //6
    private var timer = Timer()
    private var task = object : TimerTask() {
        override fun run() {
            if (snake.size >= 6 && isGameStarted) move()
        }
    }

    //7
    private fun move() {
        val item: ArrayList<Int> = when (snake_dest) {
            is Dest.W -> arrayListOf(snake[0][0] - 1, snake[0][1])
            is Dest.A -> arrayListOf(snake[0][0], snake[0][1] - 1)
            is Dest.S -> arrayListOf(snake[0][0] + 1, snake[0][1])
            is Dest.D -> arrayListOf(snake[0][0], snake[0][1] + 1)
        }

        var wasApple = false
        var gameIsOver = false

        when (targetCtrl(item)) {
            Target.WALL -> {
                gameOver("Wall")
                gameIsOver = true
            }

            Target.AIR -> snake.removeAt(snake.size - 1)
            Target.BODY -> {
                gameOver("Body")
                gameIsOver = true
            }

            Target.APPLE -> {
                score.value++
                wasApple = true
            }
        }
        if (!gameIsOver) {
            snake.add(0, item)
        }
        clearDraw {
            drawWall()
            drawSnake()
            if (wasApple) {
                drawApple()
            }
        }
    }

    private fun gameOver(end: String) {
        alertDialogVisible.value = true
        alertDialogMessage.value = "$end end! Game over"
        isGameStarted = false
        score.value = 0
        snake.clear()
        snake = arrayListOf(
            arrayListOf(snake_y, 7),
            arrayListOf(snake_y, 6),
            arrayListOf(snake_y, 5),
            arrayListOf(snake_y, 4),
            arrayListOf(snake_y, 3),
            arrayListOf(snake_y, 2)
        )
        clearDraw {
            drawWall()
            drawSnake()
        }
    }

    private fun targetCtrl(item: ArrayList<Int>): Target {
        return when (table[item[0]][item[1]].value) {
            appleChar -> Target.APPLE
            snakeBody -> Target.BODY
            wallChar -> Target.WALL
            else -> Target.AIR
        }
    }

    //8
    private fun clearDraw(drawAgain: () -> Unit) {
        for (y in 0 until table_size) {
            for (x in 0 until table_size) {
                if (table[y][x].value != appleChar) {
                    table[y][x].value = tableBgChar
                }
            }
        }
        drawAgain()
    }
}