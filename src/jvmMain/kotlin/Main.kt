import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import sealed.Dest
import sealed.Target
import ui.MainScreen
import java.util.*
import kotlin.collections.ArrayList

var table_size = 30
var table = Array(table_size) { Array(table_size) { _ -> mutableStateOf(' ') } }

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
    Game().startGame()
}

class Game {

    private val snakeHead = 'O'
    private val snakeBody = '$'
    private val appleChar = '@'
    private val tableBgChar = ' '
    private val wallChar = 'X'

    fun startGame() = application {
        Window(onCloseRequest = ::exitApplication) {
            MainScreen {
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
        drawApple()
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
                if (table[y][x].value == tableBgChar) {
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
            "e" -> {
                try {
                    timer.cancel()
                } catch (_: Exception) {
                }
                return
            }
            else -> {
                println(">invalid destination")
                return
            }
        }
        if (!isGameStarted) {
            timer.scheduleAtFixedRate(task, 0, 500)
            isGameStarted = true
        }
    }

    //6
    var timer = Timer()
    val task = object : TimerTask() {
        override fun run() {
            if (snake.size >= 6) move()
        }
    }

    //7
    private fun move() {
        lateinit var item: ArrayList<Int>
        when (snake_dest) {
            is Dest.W -> item = arrayListOf(snake[0][0] - 1, snake[0][1])
            is Dest.A -> item = arrayListOf(snake[0][0], snake[0][1] - 1)
            is Dest.S -> item = arrayListOf(snake[0][0] + 1, snake[0][1])
            is Dest.D -> item = arrayListOf(snake[0][0], snake[0][1] + 1)
        }
        when (targetCtrl(item)) {
            Target.WALL -> gameOver("Wall")
            Target.AIR -> snake.removeAt(snake.size - 1)
            Target.BODY -> gameOver("Body")
            Target.APPLE -> {}
        }
        snake.add(0, item)
        clearDraw {
            drawWall()
            drawSnake()
        }
    }

    private fun gameOver(end: String) {
        println("$end end! Game over")
        timer.cancel()
        //timer = Timer()
        isGameStarted = false
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
        for (y in 0..table_size - 1) {
            for (x in 0..table_size - 1) {
                table[y][x].value = tableBgChar
            }
        }
        drawAgain()
    }
}