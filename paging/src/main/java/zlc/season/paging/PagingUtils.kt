package zlc.season.paging

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import zlc.season.ironbranch.isMainThread


fun log(str: String) {
    Log.d("Paging", str)
}

fun ViewGroup.inflate(res: Int, attach: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(res, this, attach)
}

fun assertMainThread(block: () -> Unit) {
    if (isMainThread()) {
        block()
    } else {
        throw IllegalThreadStateException("Must main thread!")
    }
}

fun <T> listOf(vararg params: List<T>): List<T> {
    val result = mutableListOf<T>()
    params.forEach {
        result.addAll(it)
    }
    return result
}

fun <T> sizeOf(vararg params: List<T>): Int {
    var size = 0
    params.forEach {
        size += it.size
    }
    return size
}

fun <T> clearOf(vararg params: MutableList<T>) {
    params.forEach {
        it.clear()
    }
}
