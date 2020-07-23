package com.example.karma.dummy

class HeaderModel(private val section: Int) : Section {
    override var name: String? = null
    fun setheader(header: String?) {
        name = header
    }

    override val isHeader: Boolean
        get() = true

    override fun sectionPosition(): Int {
        return section
    }

}