package com.vasanth.fscolorreplace

import com.vasanth.fscolorreplace.helper.ColorReader
import com.vasanth.fscolorreplace.helper.ColorReplaceFileVisitor
import java.nio.file.Files
import java.nio.file.Paths

class FSColorReplace {

    companion object {

        private const val PROJECT_PATH = "/Users/vaannadurai/Workspace/Android/Freshworks/Freshservice Review"
        const val COLOR_FILE_PATH =
            "/Users/vaannadurai/Vasanth/Company/Products/Freshservice/Tech/Dark Theme/Color System - Tokens - Mapping.csv"
        const val IS_LIGHT_THEME = true

        @JvmStatic
        fun main(args: Array<String>) {
            // Read Colors from File.
            val fsColors = ColorReader.readColorsFromFile()

            // Walk File Tree.
            val startingDir = Paths.get(PROJECT_PATH)
            val fileVisitor = ColorReplaceFileVisitor(fsColors = fsColors)
            Files.walkFileTree(startingDir, fileVisitor)
        }
    }
}