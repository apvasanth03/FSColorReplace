package com.vasanth.fscolorreplace.helper

import com.vasanth.fscolorreplace.model.FSColor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
import java.nio.file.StandardOpenOption.WRITE

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

        fsColors.forEach { fsColor ->
            with(fsColor) {
                val newColor = if (isLightTheme) newColorLight else newColorDark

                oldColors.forEach { oldColor ->
                    updatedContent = updatedContent.replace(
                        oldValue = oldColor,
                        newValue = newColor,
                        ignoreCase = true
                    )
                }
            }
        }

        return updatedContent
    }
    // endregion
}