package zlc.season.sange

import android.support.v7.widget.RecyclerView
import zlc.season.ironbranch.ioThread
import zlc.season.ironbranch.mainThread
import java.util.concurrent.atomic.AtomicBoolean

open class DataSource<T> {

    class Config(
            val useDiff: Boolean = true
    )

    protected open val dataStorage = DataStorage<T>()

    private val fetchingState = FetchingState()
    private val invalid = AtomicBoolean(false)
    private var pagingListDiffer = PagingListDiffer<T>()

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
     * Notify submit list.
     */
    fun notifySubmitList(initial: Boolean = false) {
        ensureMainThread {
            pagingListDiffer.submitList(dataStorage.all(), initial)
        }
    }

    /**
     * Data functions
     */
    fun clear(delay: Boolean = false) {
        ensureMainThread {
            dataStorage.clear()
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun add(t: T, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.add(position, t)
            } else {
                dataStorage.add(t)
            }

            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun addAll(list: List<T>, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addAll(position, list)
            } else {
                dataStorage.addAll(list)
            }
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun removeAt(position: Int, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.removeAt(position)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun remove(t: T, delay: Boolean = false) {
        ensureMainThread {
            val index = dataStorage.indexOf(t)
            if (index != -1) {
                dataStorage.remove(t)
                if (!delay) {
                    notifySubmitList()
                }
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }

    fun set(old: T, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.set(old, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun set(index: Int, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.set(index, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun get(position: Int): T {
        return pagingListDiffer.get(position)
    }

    fun size(): Int {
        return pagingListDiffer.size()
    }

    /**
     * Use [loadInitial] for initial loading, use [LoadCallback] callback
     * to set the result after loading is complete.
     */
    open fun loadInitial(loadCallback: LoadCallback<T>) {}

    /**
     * Use [loadAfter] for load next page, use [LoadCallback] callback
     * to set the result after loading is complete.
     */
    open fun loadAfter(loadCallback: LoadCallback<T>) {}

    internal fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        pagingListDiffer.adapter = adapter
        if (adapter != null) {
            invalidate()
        }
    }

    internal fun getItemCount() = size()

    internal fun getItem(position: Int): T {
        dispatchLoadAround(position)
        return get(position)
    }

    private fun dispatchLoadInitial() {
        ioThread {
            loadInitial(object : LoadCallback<T> {
                override fun setResult(data: List<T>?, delay: Boolean) {
                    mainThread {
                        if (!data.isNullOrEmpty()) {
                            dataStorage.addAll(data)
                            if (!delay) {
                                notifySubmitList(true)
                            }
                        }
                        invalid.compareAndSet(true, false)

                        fetchingState.setState(FetchingState.READY_TO_FETCH)
                    }
                }
            })
        }
    }

    private fun dispatchLoadAround(position: Int) {
        if (isInvalid()) return

        if (position == getItemCount() - 1) {
            scheduleLoadAfter()
            return
        }
    }

    private fun scheduleLoadAfter() {
        if (fetchingState.isNotReady()) {
            return
        }

        fetchingState.setState(FetchingState.FETCHING)

        ioThread {
            loadAfter(object : LoadCallback<T> {
                override fun setResult(data: List<T>?, delay: Boolean) {
                    if (isInvalid()) return
                    mainThread {
                        if (isInvalid()) return@mainThread

                        if (data != null) {
                            if (data.isEmpty()) {
                                fetchingState.setState(FetchingState.DONE_FETCHING)
                            } else {
                                dataStorage.addAll(data)

                                if (!delay) {
                                    notifySubmitList()
                                }
                                fetchingState.setState(FetchingState.READY_TO_FETCH)
                            }
                        } else {
                            fetchingState.setState(FetchingState.FETCHING_ERROR)
                        }
                    }
                }
            })
        }
    }

    private fun isInvalid(): Boolean {
        return invalid.get()
    }

    interface LoadCallback<T> {
        fun setResult(data: List<T>?, delay: Boolean = false)
    }
}