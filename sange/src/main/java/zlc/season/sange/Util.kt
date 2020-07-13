package zlc.season.sange

import android.os.Looper
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun <T> T?.cleanUpItem() {
    this?.let {
        if (this is Cleanable) {
            this.cleanUp()
        }
    }
}

fun <T> List<T>.cleanUp() {
    this.forEach {
        it.cleanUpItem()
    }
}

fun isMainThread(): Boolean {
    return Looper.getMainLooper().thread === Thread.currentThread()
}

fun ensureMainThread(block: () -> Unit) {
    if (isMainThread()) {
        block()
    } else {
        GlobalScope.launch(Main) {
            block()
        }
    }
}

fun <T> assertMainThreadWithResult(block: () -> T): T {
    if (isMainThread()) {
        return block()
    } else {
        throw IllegalStateException("This operation only supports the Main thread call!")
    }
}

fun launchIo(block: suspend () -> Unit) {
    GlobalScope.launch(IO) {
        block()
    }
}

fun launchMain(block: suspend () -> Unit) {
    GlobalScope.launch(Main) {
        block()
    }
}
