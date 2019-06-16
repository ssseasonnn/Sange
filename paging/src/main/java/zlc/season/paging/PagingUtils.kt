package zlc.season.paging

import zlc.season.ironbranch.isMainThread
import zlc.season.ironbranch.mainThread

fun ensureMainThread(block: () -> Unit) {
    if (isMainThread()) {
        block()
    } else {
        mainThread {
            block()
        }
    }
}
