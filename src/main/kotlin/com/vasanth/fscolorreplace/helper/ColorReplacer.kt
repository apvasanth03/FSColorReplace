package com.vasanth.fscolorreplace.helper

import com.vasanth.fscolorreplace.model.FSColor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
import java.nio.file.StandardOpenOption.WRITE
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * [ColorReplacer] - Replaces Color in the given file.
 */
object ColorReplacer {

    /**
     * Replace Color in the given File.
     */
    fun replaceColorInFile(
        file: Path,
        fsColors: List<FSColor>,
        isLightTheme: Boolean,
    ) {
        try {
            // Read file
            val originalContent = readContentFromFile(file = file)

            // Replace Colors
            val updatedContent = replaceColorInContent(
                content = originalContent,
                fsColors = fsColors,
                isLightTheme = isLightTheme
            )

            // Write File
            writeContentToFile(file = file, content = updatedContent)
        } catch (exp: Exception) {
            println("Failed File - $file")
            exp.printStackTrace()
        }
    }

    /**
     * Read Content from File.
     */
    private fun readContentFromFile(file: Path): String {
        val content = Files.readString(file)
        return content
    }

    /**
     * Write Content into File.
     */
    private fun writeContentToFile(
        file: Path,
        content: String
    ) {
        Files.writeString(file, content, WRITE, TRUNCATE_EXISTING)
    }

    /**
     * Replace Color in the File Content.
     */
    private fun replaceColorInContent(
        content: String,
        fsColors: List<FSColor>,
        isLightTheme: Boolean
    ): String {
        var updatedContent = content

        // Replace - FSColors
        updatedContent = replaceFSColors(
            content = content,
            fsColors = fsColors,
            isLightTheme = isLightTheme
        )

        // Replace Black and White Colors - Only for Dark Theme.
        if (!isLightTheme) {
            updatedContent = replaceBlackAndWhiteColors(
                content = updatedContent
            )
        }

        return updatedContent
    }

    private fun replaceFSColors(
        content: String,
        fsColors: List<FSColor>,
        isLightTheme: Boolean
    ): String {
        var updatedContent = content

        fsColors.forEach { fsColor ->
            with(fsColor) {
                val newColor = if (isLightTheme) newColorLight else newColorDark

                oldColors.forEach { oldColor ->
                    /**
                     * Match any of
                     * #FFFFFFFF OR #FFFFFF
                     * 0XFFFFFFFF OR 0XFFFFFF
                     */
                    val regex = "(#|0X)(\\w\\w)?($oldColor)".toRegex(option = IGNORE_CASE)
                    val replaceColor = "$1$2$newColor"

                    updatedContent = updatedContent.replace(
                        regex = regex,
                        replacement = replaceColor,
                    )
                }
            }
        }

        return updatedContent
    }

    fun replaceBlackAndWhiteColors(
        content: String
    ): String {
        val regex = "(#|0X)(\\w\\w)?(FFFFFF|000000)".toRegex(option = IGNORE_CASE)

        val updatedContent = content.replace(
            regex = regex
        ) { result ->
            val colorPrefix = result.groupValues[1]
            val colorAlpha = result.groupValues[2]
            val colorValue = result.groupValues[3]

            val replaceColorValue = if (colorValue.equals("FFFFFF", ignoreCase = true)) {
                "000000"
            } else {
                "FFFFFF"
            }

            "$colorPrefix$colorAlpha$replaceColorValue"
        }

        return updatedContent
    }
    // endregion
}

fun main() {
    val content = "#FFFFFFFF #ffffff 0XFFFFFFFF 0XFFFFFF #000000FF #000000 0X000000FF 0X000000"
    val updatedContent = ColorReplacer.replaceBlackAndWhiteColors(content = content)
    println(updatedContent)
}