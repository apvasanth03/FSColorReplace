package com.vasanth.fscolorreplace.helper

import com.opencsv.CSVReader
import com.vasanth.fscolorreplace.model.FSColor
import java.io.FileReader

/**
 * [ColorReader] - Used to read Colors from CSV File.
 */
object ColorReader {

    /**
     * Read Colors CSV file and convert it into [FSColor]
     */
    fun readColorsFromFile(): List<FSColor> {
        val csvReader = CSVReader(FileReader("/Users/vaannadurai/Downloads/Color System - Tokens - Mapping.csv"))
        
        val fsColors = csvReader.use {
            val rows = csvReader.readAll()
            val fsColors = rows.mapNotNull {
                parseFSColor(row = it)
            }
            fsColors
        }

        return fsColors
    }

    /**
     * Parse individual Row from CSV File.
     */
    private fun parseFSColor(
        row: Array<String>
    ): FSColor? {
        var fsColor: FSColor? = null

        if (row.size >= 4) {
            val newColorLight = parseColorValue(
                value = row[1]
            )

            val oldColors = parseColorsToMap(
                colorsToMap = row[2]
            )

            val newColorDark = parseColorValue(
                value = row[3]
            )

            if (newColorLight.isNotEmpty() &&
                oldColors.isNotEmpty() &&
                newColorDark.isNotEmpty()
            ) {
                fsColor = FSColor(
                    oldColors = oldColors,
                    newColorLight = newColorLight,
                    newColorDark = newColorDark
                )
            }
        }

        return fsColor
    }

    /**
     * parse Light & Dark Color Value Column
     *
     * IP - "#EAEEF2"
     * OP - "EAEEF2"
     */
    private fun parseColorValue(value: String): String {
        val op = value.trim().replace("#", "")
        return op
    }

    /**
     * parse "Color to Map" Column
     *
     * IP - "#314251, #3E3E3E"
     * OP - listOf("314251", "3E3E3E")
     */
    private fun parseColorsToMap(
        colorsToMap: String
    ): List<String> {
        val colors = colorsToMap.split(",")
            .map { color ->
                color.trim()
            }
            .mapNotNull { color ->
                if (color.isNotEmpty()) {
                    parseColorValue(value = color)
                } else {
                    null
                }
            }

        return colors
    }
}