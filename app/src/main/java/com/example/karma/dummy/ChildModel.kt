package com.example.karma.dummy

class ChildModel(private val section: Int) : Section {
    override var name: String? = null
    fun setChild(child: String?) {
        name = child
    }

    override val isHeader: Boolean
        get() = false

    override fun sectionPosition(): Int {
        return section
    }

}