package zlc.season.paging

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

fun ioThread(block: () -> Unit) {
    diskIO.execute(block)
}

fun mainThread(block: () -> Unit) {
    mainHandler.post(block)
}

fun isMainThread(): Boolean {
    return Looper.getMainLooper().thread === Thread.currentThread()
}

private val mainHandler by lazy { Handler(Looper.getMainLooper()) }

private val diskIO by lazy {
    Executors.newFixedThreadPool(4, object : ThreadFactory {
        private val THREAD_NAME_STEM = "season_disk_io_%d"

        private val mThreadId = AtomicInteger(0)

        override fun newThread(r: Runnable): Thread {
            val t = Thread(r)
            t.name = String.format(THREAD_NAME_STEM, mThreadId.getAndIncrement())
            return t
        }
    })
}