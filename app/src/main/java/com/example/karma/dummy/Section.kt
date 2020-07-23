package com.example.karma.dummy

interface Section {
    val isHeader: Boolean
    val name: String?

    fun sectionPosition(): Int
}
