package zlc.season.sangedemo.demo.multi

import zlc.season.sange.SangeItem

const val NORMAL = 0
const val HEADER = 1
const val FOOTER = 2
const val STATE = 3

open class NormalItem(val i: Int) : SangeItem {
    override fun viewType() = NORMAL

    override fun toString() = "Item $i"
}

class HeaderItem(val i: Int) : SangeItem {
    override fun viewType() = HEADER

    override fun toString() = "Header $i"
}

class FooterItem(val i: Int) : SangeItem {
    override fun viewType() = FOOTER

    override fun toString() = "Footer $i"
}

class StateItem(val state: Int, val retry: () -> Unit) : SangeItem {
    override fun viewType() = STATE
}