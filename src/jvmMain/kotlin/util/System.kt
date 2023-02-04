package util

import androidx.compose.ui.graphics.Color
import java.io.File

fun writeToFile(attr: String, value: String) {
    val path = "localStorage.txt"

    try {
        var getContent = ""
        if (File(path).canRead()) {
            val content = File(path).readText().lines()
            for (line in content) {
                if (!line.contains(attr)) {
                    getContent += line + "\r\n"
                }
            }
        }
        val newContent = "$attr : $value\r\n"
        File(path).bufferedWriter().use { writer ->
            writer.write(getContent)
            writer.write(newContent)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun getAttr(attr: String) : String? {
    val path = "localStorage.txt"

    try {
        val data = (File(path).readText())
        if (data.isNotEmpty()) {
            for (line in data.lines()) {
                if (line.contains(attr)) {
                    return ("$line : ").split(" : ")[1].trim()
                }
            }
        }
        return null
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun getColorFromString(content: String) : Color {
    return Color(content.substring(1).toInt(16))
}