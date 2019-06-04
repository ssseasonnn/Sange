package zlc.season.paging

import android.support.v7.util.ListUpdateCallback
import zlc.season.ironbranch.ioThread
import zlc.season.ironbranch.mainThread
import java.util.concurrent.atomic.AtomicBoolean

open class DataSource<T>(private val config: Config = Config()) {

    class Config(
            val useDiff: Boolean = true
    )

    protected open val dataStorage = DataStorage<T>()

    private var lastPosition = -1
    private val fetchingState = FetchingState()
    private val invalid = AtomicBoolean(false)

    private var pagingListDiffer = PagingListDiffer<T>()

    fun setListCallback(callback: ListUpdateCallback) {
        pagingListDiffer.updateCallback = callback
        pagingListDiffer.submitList(dataStorage.all())

    }

    protected fun notifySubmitList() {
        pagingListDiffer.submitList(dataStorage.all())
    }


    /**
     * Make this data source invalid and reload initial
     */
    fun invalidate(clear: Boolean = true) {
        assertMainThread {
            if (invalid.compareAndSet(false, true)) {
                if (clear) {
                    dataStorage.clear()
                }
                dispatchLoadInitial()
            }
        }
    }

    fun size() = dataStorage.size()

    fun get(position: Int) = dataStorage.get(position)

    internal fun getItemCount() = size()

    internal fun getItem(position: Int): T {
        dispatchLoadAround(position)
        lastPosition = position
        return get(position)
    }

    open fun loadBefore(loadCallback: LoadCallback<T>) {
        loadCallback.setResult(null)
    }

    open fun loadInitial(loadCallback: LoadCallback<T>) {}

    open fun loadAfter(loadCallback: LoadCallback<T>) {}

    private fun dispatchLoadInitial() {
        log("dispatchLoadInitial --> ${Thread.currentThread().name}")
        scheduleFetching(function = ::loadInitial, isInitial = true) {
            dataStorage.addAll(it)
            notifySubmitList()
        }
    }

    private fun dispatchLoadAround(position: Int) {
        log("dispatchLoadAround-->${Thread.currentThread().name}")
        if (isInvalid()) return

        if (isInvalidPosition(position)) {
            return
        }

        if (fetchingState.isNotReady()) return

        if (reachEndBoundary(position)) {
            log("load after --> ${Thread.currentThread().name}")
            scheduleFetching(function = ::loadAfter) {
                dataStorage.addAll(it)
                notifySubmitList()
            }
            return
        }

        if (reachStartBoundary(position)) {
            log("load before --> ${Thread.currentThread().name}")
            scheduleFetching(function = ::loadBefore) {
                dataStorage.addAll(0, it)
                notifySubmitList()
            }
            return
        }
    }

    private fun scheduleFetching(
            function: (LoadCallback<T>) -> Unit,
            isInitial: Boolean = false,
            block: (List<T>) -> Unit
    ) {
        fetchingState.setState(FetchingState.FETCHING)

        ioThread {
            function(object : LoadCallback<T> {
                override fun setResult(data: List<T>?) {
                    mainThread {
                        if (!isInitial) {
                            if (isInvalid()) return@mainThread
                        }
                        if (data != null) {
                            block(data)

                            if (data.isEmpty()) {
                                fetchingState.setState(FetchingState.DONE_FETCHING)
                            } else {
                                fetchingState.setState(FetchingState.READY_TO_FETCH)
                            }
                        } else {
                            fetchingState.setState(FetchingState.FETCHING_ERROR)
                        }

                        if (isInitial) {
                            invalid.compareAndSet(true, false)
                        }
                    }
                }
            })
        }
    }

    private fun isInvalid(): Boolean {
        return invalid.get()
    }

    open fun isInvalidPosition(position: Int): Boolean {
        return position < 0 || position >= size()
    }

    open fun reachEndBoundary(position: Int): Boolean {
        return lastPosition < position && position == size() - 1
    }

    open fun reachStartBoundary(position: Int): Boolean {
        return lastPosition > position && position == 0
    }


    interface LoadCallback<T> {
        fun setResult(data: List<T>?)
    }
}