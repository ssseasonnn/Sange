package zlc.season.paging

import android.support.v7.widget.RecyclerView
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

    protected open val dataStorage = DataStorage<T>()

    private val stateMap = mutableMapOf<Direction, FetchingState>()

    private val invalid = AtomicBoolean(false)

    private var pagingListDiffer = PagingListDiffer<T>()

    init {
        stateMap[Direction.BEFORE] = FetchingState()
        stateMap[Direction.AFTER] = FetchingState()
    }


    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        pagingListDiffer.adapter = adapter
    }

    protected fun notifySubmitList(initial: Boolean = false) {
        pagingListDiffer.submitList(dataStorage.all(), initial)
    }


    /**
     * Invalidate data source.
     */
    fun invalidate() {
        ensureMainThread {
            if (invalid.compareAndSet(false, true)) {
                dataStorage.clear()
                dispatchLoadInitial()
            }
        }
    }

    /**
     * Data functions
     */
    fun clear() {
        ensureMainThread {
            dataStorage.clear()
            notifySubmitList()
        }
    }

    fun add(t: T, position: Int = -1) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.add(position, t)
            } else {
                dataStorage.add(t)
            }

            notifySubmitList()
        }
    }

    fun addAll(list: List<T>, position: Int = -1) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addAll(position, list)
            } else {
                dataStorage.addAll(list)
            }
            notifySubmitList()
        }
    }

    fun removeAt(position: Int) {
        ensureMainThread {
            dataStorage.removeAt(position)
            notifySubmitList()
        }
    }

    fun remove(t: T) {
        ensureMainThread {
            val index = dataStorage.indexOf(t)
            if (index != -1) {
                dataStorage.remove(t)
                notifySubmitList()
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }

    fun set(old: T, new: T) {
        ensureMainThread {
            dataStorage.set(old, new)
            notifySubmitList()
        }
    }

    fun set(index: Int, new: T) {
        ensureMainThread {
            dataStorage.set(index, new)
            notifySubmitList()
        }
    }

    fun get(position: Int): T {
        return pagingListDiffer.get(position)
    }

    fun size(): Int {
        return pagingListDiffer.size()
    }

    internal fun getItemCount() = size()

    internal fun getItem(position: Int): T {
        dispatchLoadAround(position)
        return get(position)
    }

    open fun loadBefore(loadCallback: LoadCallback<T>) {
        loadCallback.setResult(emptyList())
    }

    open fun loadInitial(loadCallback: LoadCallback<T>) {}

    open fun loadAfter(loadCallback: LoadCallback<T>) {}

    private fun dispatchLoadInitial() {
        log("load initial start")
        ioThread {
            loadInitial(object : LoadCallback<T> {
                override fun setResult(data: List<T>?) {
                    mainThread {
                        if (data != null) {
                            dataStorage.addAll(data)
                            notifySubmitList(true)
                        }
                        invalid.compareAndSet(true, false)

                        setFetchingState(Direction.BEFORE, FetchingState.READY_TO_FETCH)
                        setFetchingState(Direction.AFTER, FetchingState.READY_TO_FETCH)

                        log("load initial stop")
                    }
                }
            })
        }
    }

    var lastPosition = -1

    fun dispatchLoadAround(position: Int = lastPosition) {
        lastPosition = position

        log("dispatch load around")
        if (isInvalid()) return

        if (position == getItemCount() - 1) {
            log("load after start")
            scheduleFetching(::loadAfter, Direction.AFTER) {
                dataStorage.addAll(it)
                notifySubmitList()
                log("load after stop")
            }
            return
        }

        if (position == 0) {
            log("load before start")
            scheduleFetching(::loadBefore, Direction.BEFORE) {
                dataStorage.addAll(0, it)
                notifySubmitList()
                log("load before stop")
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