package zlc.season.paging

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import zlc.season.ironbranch.isMainThread
import zlc.season.ironbranch.mainThread


fun log(str: String) {
    Log.d("Paging", str)
}

fun ViewGroup.inflate(res: Int, attach: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(res, this, attach)
}

fun ensureMainThread(block: () -> Unit) {
    if (isMainThread()) {
        block()
    } else {
        mainThread {
            block()
        }
    }
}
