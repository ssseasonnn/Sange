package zlc.season.sange

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

open class DataSource<T>(protected val coroutineScope: CoroutineScope = GlobalScope) {
    protected open val dataStorage = DataStorage<T>()

    private val pagingListDiffer = SangeListDiffer<T>(coroutineScope)

    private val fetchingState = FetchingState()
    private val invalid = AtomicBoolean(false)

    private var retryFunc: () -> Unit = {}

    /**
     * Retry fetching.
     */
    fun retry() {
        coroutineScope.ensureMainThread {
            retryFunc()
        }
    }

    /**
     * Invalidate the current data source.
     */
    fun invalidate(clear: Boolean = true) {
        coroutineScope.ensureMainThread {
            if (invalid.compareAndSet(false, true)) {
                dispatchLoadInitial(clear)
            }
        }
    }

    /**
     * Notify submit list.
     */
    fun notifySubmitList(submitNow: Boolean = false) {
        coroutineScope.ensureMainThread {
            pagingListDiffer.submitList(dataStorage.toList(), submitNow = submitNow)
        }
    }

    fun totalSize(): Int = dataStorage.totalSize()

    fun clearAll(delay: Boolean = false) {
        coroutineScope.ensureMainThread {
            dataStorage.clearAll()
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun clearItem(delay: Boolean = false) {
        coroutineScope.ensureMainThread {
            dataStorage.clearItem()
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun addItem(t: T, position: Int = -1, delay: Boolean = false) {
        coroutineScope.ensureMainThread {
            if (position > -1) {
                dataStorage.addItem(position, t)
            } else {
                dataStorage.addItem(t)
            }

            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun addItems(list: List<T>, position: Int = -1, delay: Boolean = false) {
        coroutineScope.ensureMainThread {
            if (position > -1) {
                dataStorage.addItems(position, list)
            } else {
                dataStorage.addItems(list)
            }
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun removeItemAt(position: Int, delay: Boolean = false) {
        coroutineScope.ensureMainThread {
            dataStorage.removeItemAt(position)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun removeItem(t: T, delay: Boolean = false) {
        coroutineScope.ensureMainThread {
            val index = dataStorage.indexItemOf(t)
            if (index != -1) {
                dataStorage.removeItem(t)
                if (!delay) {
                    notifySubmitList()
                }
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }

    fun setItem(old: T, new: T, delay: Boolean = false) {
        coroutineScope.ensureMainThread {
            dataStorage.setItem(old, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun setItem(index: Int, new: T, delay: Boolean = false) {
        coroutineScope.ensureMainThread {
            dataStorage.setItem(index, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun getItem(position: Int): T {
        return assertMainThreadWithResult {
            dataStorage.getItem(position)
        }
    }

    fun getItems(): List<T> {
        return assertMainThreadWithResult {
            dataStorage.getItemList()
        }
    }

    fun itemSize(): Int {
        return assertMainThreadWithResult {
            dataStorage.itemSize()
        }
    }

    fun setState(newState: T?) {
        coroutineScope.ensureMainThread {
            dataStorage.setState(newState)
            notifySubmitList()
        }
    }

    fun getState(): T? {
        return assertMainThreadWithResult {
            dataStorage.getState()
        }
    }

    open suspend fun loadInitial(): List<T>? {
        return emptyList()
    }

    open suspend fun loadAfter(): List<T>? {
        return emptyList()
    }

    private fun dispatchLoadInitial(clear: Boolean) {
        if (clear) {
            dataStorage.clearAll()
        }

        coroutineScope.launchIo {
            val result = loadInitial()
            withContext(Main) {
                onLoadResult(result)
                invalid.compareAndSet(true, false)
                if (result == null) {
                    retryFunc = { dispatchLoadInitial(clear) }
                }
            }
        }
    }

    open fun shouldLoadNext(position: Int): Boolean {
        return position == totalSize() - 1
    }

    private fun dispatchLoadAround(position: Int) {
        if (isInvalid()) return

        if (shouldLoadNext(position)) {
            if (fetchingState.isNotReady()) {
                return
            }
            scheduleLoadAfter()
            return
        }
    }

    private fun scheduleLoadAfter() {
        changeState(FetchingState.FETCHING)

        coroutineScope.launchIo {
            val result = loadAfter()
            withContext(Main) {
                if (isInvalid()) return@withContext
                onLoadResult(result)

                if (result == null) {
                    retryFunc = { scheduleLoadAfter() }
                }
            }
        }
    }

    private fun onLoadResult(data: List<T>?) {
        if (data != null) {
            if (data.isEmpty()) {
                changeState(FetchingState.DONE_FETCHING)
            } else {
                dataStorage.addItems(data)
                notifySubmitList()

                changeState(FetchingState.READY_TO_FETCH)
            }
        } else {
            changeState(FetchingState.FETCHING_ERROR)
        }
    }

    private fun changeState(newState: Int) {
        fetchingState.setState(newState)
        onStateChanged(newState)
    }

    /**
     * Call on state changed.
     * @param newState May be these values:
     *  [FetchingState.FETCHING], [FetchingState.FETCHING_ERROR],
     *  [FetchingState.DONE_FETCHING], [FetchingState.READY_TO_FETCH]
     */
    protected open fun onStateChanged(newState: Int) {}

    /**
     * Clean up resources.
     */
    open fun cleanUp() {
        dataStorage.toList().cleanUp()
    }

    private fun isInvalid(): Boolean {
        return invalid.get()
    }

    // internal function for adapter
    internal fun setAdapter(adapter: RecyclerView.Adapter<*>?, shouldInvalidate: Boolean) {
        pagingListDiffer.adapter = adapter
        if (adapter != null) {
            if (shouldInvalidate) {
                invalidate(false)
            } else {
                notifySubmitList()
            }
        }
    }

    internal fun getSizeForAdapter(): Int {
        return pagingListDiffer.size()
    }

    internal fun getItemForAdapter(position: Int): T {
        dispatchLoadAround(position)
        return pagingListDiffer.get(position)
    }
}