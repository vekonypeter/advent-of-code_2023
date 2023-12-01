package vp.aoc.common

import java.io.File
import java.io.FileNotFoundException

class FileLoader {

    companion object {
        fun loadFile(path: String): File =
            File(
                Companion::class.java.classLoader
                    .getResource(path)?.toURI() ?: throw FileNotFoundException()
            )
    }
}