package com.vasanth.fscolorreplace.helper

import com.vasanth.fscolorreplace.FSColorReplace
import com.vasanth.fscolorreplace.model.FSColor
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.extension

/**
 * [ColorReplaceFileVisitor] - Java [FileVisitor]
 */
class ColorReplaceFileVisitor constructor(
    private val fsColors: List<FSColor>
) : SimpleFileVisitor<Path>() {

    override fun visitFile(
        file: Path,
        attr: BasicFileAttributes
    ): FileVisitResult {
        val supportedFileExtensions = listOf("java", "kt", "xml")

        if (attr.isRegularFile) {
            val isSupportedFile = supportedFileExtensions.contains(file.extension)
            if (isSupportedFile) {
                ColorReplacer.replaceColorInFile(
                    file = file,
                    fsColors = fsColors,
                    isLightTheme = FSColorReplace.IS_LIGHT_THEME
                )
            }
        }
        return FileVisitResult.CONTINUE
    }

    override fun preVisitDirectory(
        dir: Path,
        attrs: BasicFileAttributes
    ): FileVisitResult {
        val ignoreDirs = (dir.endsWith("build") || dir.endsWith("gradle"))

        val result = if (ignoreDirs) {
            FileVisitResult.SKIP_SUBTREE
        } else {
            FileVisitResult.CONTINUE
        }

        return result
    }
}