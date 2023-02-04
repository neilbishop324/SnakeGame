import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import sealed.Dest
import sealed.Target
import ui.mainScreen
import ui.menuScreen
import util.getAttr
import util.writeToFile
import java.util.*
import kotlin.collections.ArrayList

private const val TABLE_SIZE = 30
var table = Array(TABLE_SIZE) { y ->
    Array(TABLE_SIZE) { x ->
        if ((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0)) mutableStateOf(Color(0xFF338a05)) else mutableStateOf(
            Color(0xFF005501)
        )
    }
}

fun main() {
    Game().openGame()
}

val alertDialogVisible = mutableStateOf(false)
val alertDialogMessage = mutableStateOf("")

val menuVisible = mutableStateOf(true)
val speed = mutableStateOf(125)

class Game {

    private var isTaskStarted = false

    private val snakeHead = Color(0xFFa4a607)
    private val snakeBody = Color(0xFF8a5905)
    private val appleColor = Color.Red
    private val tableBgColorLight = Color(0xFF338a05)
    private val tableBgColorDark = Color(0xFF005501)
    private val wallColor = Color(0xFF310018)

    private val score = mutableStateOf(0)
    private val highScore = mutableStateOf(0)

    private var isGameStarted = false

    private val snakeY = TABLE_SIZE / 2
    private var snake = arrayListOf(
        arrayListOf(snakeY, 7),
        arrayListOf(snakeY, 6),
        arrayListOf(snakeY, 5),
        arrayListOf(snakeY, 4),
        arrayListOf(snakeY, 3),
        arrayListOf(snakeY, 2)
    )

    private var snakeDest: Dest = Dest.D

    fun openGame() {
        getLocalData()
        gameMenu()
    }

    private fun getLocalData() {
        getAttr("highScore")?.let { s ->
            highScore.value = s.filter { it.isDigit() }.toInt()
        }
    }

    private fun gameMenu() = application {
        val gameMenuState =
            rememberWindowState(width = 400.dp, height = 400.dp, position = WindowPosition(Alignment.Center))
        Window(
            visible = menuVisible.value,
            onCloseRequest = ::exitApplication,
            title = "Snake Game, Score: ${score.value}, High Score: ${highScore.value}",
            state = gameMenuState
        ) {
            menuScreen()
        }
        startGame(this)
    }

    @Composable
    private fun startGame(applicationScope: ApplicationScope) {
        val gameWindowState =
            rememberWindowState(width = 600.dp, height = 633.dp, position = WindowPosition(Alignment.Center))
        Window(
            visible = !menuVisible.value,
            onCloseRequest = { applicationScope.exitApplication() },
            title = "Snake Game, Score: ${score.value}, High Score: ${highScore.value}",
            state = gameWindowState
        ) {
            mainScreen(applicationScope) {
                moveControl(it)
            }
            if (!menuVisible.value) {
                drawApple()
                drawWall()
                drawSnake()
            }
        }
    }

    private fun drawWall() {
        for (y in 0 until TABLE_SIZE) {
            for (x in 0 until TABLE_SIZE) {
                if (y == 0 || y == TABLE_SIZE - 1 || x == 0 || x == TABLE_SIZE - 1) {
                    table[y][x].value = wallColor
                }
            }
        }
    }

    private fun drawSnake() {
        for (i in snake.indices) {
            table[snake[i][0]][snake[i][1]].value = if (i == 0) snakeHead else snakeBody
        }
    }

    private fun drawApple() {
        val emptyList = ArrayList<ArrayList<Int>>()
        for (y in table.indices) {
            table[y].forEach {
                if (it.value == appleColor) {
                    return
                }
            }
            for (x in table[y].indices) {
                if ((table[y][x].value == tableBgColorLight || table[y][x].value == tableBgColorDark) &&
                    !snake.contains(arrayListOf(y, x)) &&
                    (y != 0 && y != TABLE_SIZE - 1 && x != 0 && x != TABLE_SIZE - 1)
                ) {
                    emptyList.add(arrayListOf(y, x))
                }
            }
        }
        val apple = emptyList.random()
        table[apple[0]][apple[1]].value = appleColor
    }

    private fun moveControl(destination: String?) {
        when (destination?.lowercase()) {
            "w" -> snakeDest = Dest.W
            "a" -> snakeDest = Dest.A
            "s" -> snakeDest = Dest.S
            "d" -> snakeDest = Dest.D
        }

        isGameStarted = true
        alertDialogVisible.value = false

        if (!isTaskStarted) {
            timer.scheduleAtFixedRate(task, 0, speed.value.toLong())
            isTaskStarted = true
        }
    }

    private var timer = Timer()
    private var task = object : TimerTask() {
        override fun run() {
            if (snake.size >= 6 && isGameStarted) move()
        }
    }

    private fun move() {
        val item: ArrayList<Int> = when (snakeDest) {
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
        if (score.value > highScore.value) {
            highScore.value = score.value
            writeToFile("highScore", highScore.value.toString())
        }
        score.value = 0
        snake.clear()
        snake = arrayListOf(
            arrayListOf(snakeY, 7),
            arrayListOf(snakeY, 6),
            arrayListOf(snakeY, 5),
            arrayListOf(snakeY, 4),
            arrayListOf(snakeY, 3),
            arrayListOf(snakeY, 2)
        )
        clearDraw {
            drawWall()
            drawSnake()
        }
    }

    private fun targetCtrl(item: ArrayList<Int>): Target {
        return when (table[item[0]][item[1]].value) {
            appleColor -> Target.APPLE
            snakeBody -> Target.BODY
            wallColor -> Target.WALL
            else -> Target.AIR
        }
    }

    private fun clearDraw(drawAgain: () -> Unit) {
        for (y in 0 until TABLE_SIZE) {
            for (x in 0 until TABLE_SIZE) {
                if (table[y][x].value != appleColor) {
                    table[y][x].value =
                        if ((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0)) tableBgColorLight else tableBgColorDark
                }
            }
        }
        drawAgain()
    }
}