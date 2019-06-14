package zlc.season.paging

import android.support.v7.util.ListUpdateCallback
import zlc.season.ironbranch.ioThread
import zlc.season.ironbranch.mainThread
import java.util.concurrent.atomic.AtomicBoolean

open class DataSource<T> {

    class Config(
        val useDiff: Boolean = true
    )

    enum class Direction {
        BEFORE, AFTER
    }

    protected val dataStorage by lazy { onCreateStorage() }

    private val stateMap = mutableMapOf<Direction, FetchingState>()

    private val invalid = AtomicBoolean(false)

    private var pagingListDiffer = PagingListDiffer<T>()

    init {
        stateMap[Direction.BEFORE] = FetchingState()
        stateMap[Direction.AFTER] = FetchingState()
    }

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

    fun getItemCount() = pagingListDiffer.size()

    fun getItem(position: Int): T {
        return pagingListDiffer.get(position)
    }

    open fun onCreateStorage(): Storage<T> {
        return DataStorage()
    }

    open fun loadBefore(loadCallback: LoadCallback<T>) {
        loadCallback.setResult(emptyList())
    }

    open fun loadInitial(loadCallback: LoadCallback<T>) {}

    open fun loadAfter(loadCallback: LoadCallback<T>) {}

    private fun dispatchLoadInitial() {
        log("load initial")
        ioThread {
            loadInitial(object : LoadCallback<T> {
                override fun setResult(data: List<T>?) {
                    mainThread {
                        log("load initial success")
                        if (data != null) {
                            dataStorage.addAll(data)
                            notifySubmitList()
                        }
                        invalid.compareAndSet(true, false)

                        setFetchingState(Direction.BEFORE, FetchingState.READY_TO_FETCH)
                        setFetchingState(Direction.AFTER, FetchingState.READY_TO_FETCH)
                    }
                }
            })
        }
    }

    fun dispatchLoadAround(direction: Direction) {
        if (isInvalid()) return

        if (direction == Direction.AFTER) {
            scheduleFetching(::loadAfter, Direction.AFTER) {
                dataStorage.addAll(it)
                notifySubmitList()
            }
            return
        }

        if (direction == Direction.BEFORE) {
            scheduleFetching(::loadBefore, Direction.BEFORE) {
                dataStorage.addAll(0, it)
                notifySubmitList()
            }
            return
        }
    }

    private fun scheduleFetching(
        function: (LoadCallback<T>) -> Unit,
        direction: Direction,
        block: (List<T>) -> Unit
    ) {
        if (getState(direction).isNotReady()) {
            return
        }

        setFetchingState(direction, FetchingState.FETCHING)

        ioThread {
            function(object : LoadCallback<T> {
                override fun setResult(data: List<T>?) {
                    mainThread {
                        if (isInvalid()) return@mainThread

                        if (data != null) {
                            block(data)

                            if (data.isEmpty()) {
                                setFetchingState(direction, FetchingState.DONE_FETCHING)
                            } else {
                                setFetchingState(direction, FetchingState.READY_TO_FETCH)
                            }
                        } else {
                            setFetchingState(direction, FetchingState.FETCHING_ERROR)
                        }
                    }
                }
            })
        }
    }

    protected open fun setFetchingState(direction: Direction, newState: Int) {
        getState(direction).setState(newState)
    }

    private fun getState(direction: Direction): FetchingState {
        return stateMap[direction]!!
    }

    private fun isInvalid(): Boolean {
        return invalid.get()
    }

    interface LoadCallback<T> {
        fun setResult(data: List<T>?)
    }
}