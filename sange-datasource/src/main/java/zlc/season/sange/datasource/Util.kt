package zlc.season.sange.datasource

import android.os.Looper
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

val mainScope = MainScope()
val TAG = "Sange"

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

fun CoroutineScope.ensureMainThread(block: () -> Unit) {
    if (isMainThread()) {
        block()
    } else {
        launch(Main) {
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

fun CoroutineScope.launchIo(block: suspend () -> Unit) {
    launch(IO) {
        block()
    }
}

fun CoroutineScope.launchMain(block: suspend () -> Unit) {
    launch(Main) {
        block()
    }
}

fun safe(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        Log.w(TAG, e.message, e)
    }
}