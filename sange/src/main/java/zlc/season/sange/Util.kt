package zlc.season.sange

import android.os.Looper
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal fun <T> T?.cleanUpItem() {
    this?.let {
        if (this is Cleanable) {
            this.cleanUp()
        }
    }
}

internal fun <T> List<T>.cleanUp() {
    this.forEach {
        it.cleanUpItem()
    }
}

internal fun isMainThread(): Boolean {
    return Looper.getMainLooper().thread === Thread.currentThread()
}

internal fun ensureMainThread(block: () -> Unit) {
    if (isMainThread()) {
        block()
    } else {
        GlobalScope.launch(Main) {
            block()
        }
    }
}

internal fun <T> assertMainThreadWithResult(block: () -> T): T {
    if (isMainThread()) {
        return block()
    } else {
        throw IllegalStateException("This operation only supports the Main thread call!")
    }
}

internal fun launchIo(block: suspend () -> Unit) {
    GlobalScope.launch(IO) {
        block()
    }
}

internal fun launchMain(block: suspend () -> Unit) {
    GlobalScope.launch(Main) {
        block()
    }
}
