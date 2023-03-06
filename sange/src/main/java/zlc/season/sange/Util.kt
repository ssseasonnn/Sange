package zlc.season.sange

import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal val mainScope = MainScope()

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

internal fun CoroutineScope.ensureMainThread(block: () -> Unit) {
    if (isMainThread()) {
        block()
    } else {
        launch(Main) {
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

internal fun CoroutineScope.launchIo(block: suspend () -> Unit) {
    launch(IO) {
        block()
    }
}

internal fun CoroutineScope.launchMain(block: suspend () -> Unit) {
    launch(Main) {
        block()
    }
}
