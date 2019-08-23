package zlc.season.sangedemo.demo

import android.util.Log
import zlc.season.sange.SangeItem
import java.io.Closeable
import kotlin.concurrent.thread

const val NORMAL = 0
const val HEADER = 1
const val FOOTER = 2
const val STATE = 3

class NormalItem(val i: Int) : SangeItem {
    override fun viewType() = NORMAL

    override fun toString() = "Item $i"
}

class HeaderItem(val i: Int) : SangeItem, Closeable {
    override fun close() {
        thread.interrupt()
        stop = true
    }

    val thread: Thread
    var stop = false

    init {
        thread = thread {
            for (i in 0..100) {
                if (stop){
                    break
                }
                Log.d("Sange", "$i")
                Thread.sleep(1000)
            }
        }

    }

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