package zlc.season.sange

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

open class DataSource<T> {
    protected open val dataStorage = DataStorage<T>()

    private val fetchingState = FetchingState()
    private val invalid = AtomicBoolean(false)
    private var pagingListDiffer = SangeListDiffer<T>()

    private var retryFunc: () -> Unit = {}

    /**
     * Retry fetching.
     */
    fun retry() {
        ensureMainThread {
            retryFunc()
        }
    }

    /**
     * Invalidate the current data source.
     */
    fun invalidate(clear: Boolean = true) {
        ensureMainThread {
            if (invalid.compareAndSet(false, true)) {
                dispatchLoadInitial(clear)
            }
        }
    }

    /**
     * Notify submit list.
     */
    fun notifySubmitList(submitNow: Boolean = false) {
        ensureMainThread {
            pagingListDiffer.submitList(dataStorage.toList(), submitNow = submitNow)
        }
    }

    /**
     * Clear all data from the current data source.
     */
    fun clearAll(delay: Boolean = false) {
        ensureMainThread {
            dataStorage.clearAll()
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    /**
     * Clear items, only items.
     */
    fun clearItem(delay: Boolean = false) {
        ensureMainThread {
            dataStorage.clearItem()
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun addItem(t: T, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
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
        ensureMainThread {
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
        ensureMainThread {
            dataStorage.removeItemAt(position)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun removeItem(t: T, delay: Boolean = false) {
        ensureMainThread {
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
        ensureMainThread {
            dataStorage.setItem(old, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun setItem(index: Int, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.setItem(index, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    /**
     * return item for [position]
     */
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

    /**
     * return item size
     */
    fun itemSize(): Int {
        return assertMainThreadWithResult {
            dataStorage.itemSize()
        }
    }

    /**
     * Set state
     */
    fun setState(newState: T?) {
        ensureMainThread {
            dataStorage.setState(newState)
            notifySubmitList()
        }
    }

    fun getState(): T? {
        return assertMainThreadWithResult {
            dataStorage.getState()
        }
    }

    /**
     * Initial loading
     */
    open suspend fun loadInitial(): List<T>? {
        return emptyList()
    }

    /**
     * Load next page
     */
    open suspend fun loadAfter(): List<T>? {
        return emptyList()
    }

    /**
     * Return total size, not just the size of items
     */
    fun size(): Int {
        return pagingListDiffer.size()
    }

    /**
     * Return data for [position]
     */
    fun get(position: Int): T {
        return pagingListDiffer.get(position)
    }

    internal fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        pagingListDiffer.adapter = adapter
        if (adapter != null) {
            invalidate(false)
        }
    }

    internal fun getItemCount(): Int {
        return size()
    }

    internal fun getItemInner(position: Int): T {
        dispatchLoadAround(position)
        return this.get(position)
    }

    private fun dispatchLoadInitial(clear: Boolean) {
        if (clear) {
            dataStorage.clearAll()
        }

        launchIo {
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
        return position == size() - 1
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

        launchIo {
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
}