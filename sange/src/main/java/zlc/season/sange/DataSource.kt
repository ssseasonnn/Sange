package zlc.season.sange

import android.support.v7.widget.RecyclerView
import zlc.season.ironbranch.assertMainThreadWithResult
import zlc.season.ironbranch.ensureMainThread
import zlc.season.ironbranch.ioThread
import zlc.season.ironbranch.mainThread
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
    fun notifySubmitList() {
        ensureMainThread {
            pagingListDiffer.submitList(dataStorage.toList())
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
    fun setState(newState: T) {
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
     * Use [loadInitial] for initial loading, use [LoadCallback] callback
     * to setItem the result after loading is complete.
     */
    open fun loadInitial(loadCallback: LoadCallback<T>) {}

    /**
     * Use [loadAfter] for load next page, use [LoadCallback] callback
     * to setItem the result after loading is complete.
     */
    open fun loadAfter(loadCallback: LoadCallback<T>) {}

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

        ioThread {
            loadInitial(object : LoadCallback<T> {
                override fun setResult(data: List<T>?) {
                    mainThread {
                        onLoadResult(data)
                        invalid.compareAndSet(true, false)
                        if (data == null) {
                            retryFunc = { dispatchLoadInitial(clear) }
                        }
                    }
                }
            })
        }
    }

    private fun dispatchLoadAround(position: Int) {
        if (isInvalid()) return

        if (position == size() - 1) {
            if (fetchingState.isNotReady()) {
                return
            }
            scheduleLoadAfter()
            return
        }
    }

    private fun scheduleLoadAfter() {
        changeState(FetchingState.FETCHING)
        ioThread {
            loadAfter(object : LoadCallback<T> {
                override fun setResult(data: List<T>?) {
                    if (isInvalid()) return

                    mainThread {
                        if (isInvalid()) return@mainThread
                        onLoadResult(data)

                        if (data == null) {
                            retryFunc = { scheduleLoadAfter() }
                        }
                    }
                }
            })
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

    private fun isInvalid(): Boolean {
        return invalid.get()
    }

    interface LoadCallback<T> {
        fun setResult(data: List<T>?)
    }
}