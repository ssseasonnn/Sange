package zlc.season.paging

import android.support.v7.util.ListUpdateCallback
import zlc.season.ironbranch.ioThread
import zlc.season.ironbranch.mainThread
import java.util.concurrent.atomic.AtomicBoolean

open class DataSource<T> {

    interface Factory<T> {
        fun create(): DataSource<T>
    }

    private val dataStore = DataStore<T>()
    private val loadState = LoadState()

    private val invalid = AtomicBoolean(false)

    private var listUpdateCallback: ListUpdateCallback? = null

    fun setUpdateCallback(callback: ListUpdateCallback) {
        this.listUpdateCallback = callback
    }

    /**
     * Make this data source invalid and reload initial
     */
    fun invalidate() {
        if (invalid.compareAndSet(false, true)) {

            dispatchLoadInitial()
        }
    }

    fun getItemCount(): Int {
        return dataStore.getItemCount()
    }

    fun getItem(position: Int): T {
        dispatchLoadAround(position)
        return dataStore.getItem(position)
    }

    open fun loadBefore(loadCallback: LoadCallback<T>) {}

    open fun loadInitial(loadCallback: LoadCallback<T>) {}

    open fun loadAfter(loadCallback: LoadCallback<T>) {}

    private fun dispatchLoadInitial() {
        loadState.setState(LoadState.FETCHING)

        ioThread {
            loadInitial(object : LoadCallback<T> {
                override fun setResult(data: List<T>?) {
                    mainThread {
                        dispatchLoadResult(data) { data ->
                            dataStore.submitData(data, true)
                            listUpdateCallback?.onInserted(0, data.size)
                        }
                        invalid.compareAndSet(true, false)
                    }
                }
            })
        }
    }

    private fun dispatchLoadAround(position: Int) {
        if (isInvalid()) return

        if (loadState.isNotReady()) return

        if (dataStore.isAfterBoundary(position)) {
            loadState.setState(LoadState.FETCHING)

            ioThread {
                loadAfter(object : LoadCallback<T> {
                    override fun setResult(data: List<T>?) {
                        if (isInvalid()) return

                        mainThread {
                            if (isInvalid()) {
                                return@mainThread
                            }

                            dispatchLoadResult(data) { data ->
                                dataStore.submitData(data)
                                listUpdateCallback?.onInserted(getItemCount(), data.size)
                            }
                        }
                    }
                })
            }
        }

    }

    private fun dispatchLoadResult(data: List<T>?, block: (List<T>) -> Unit) {
        if (data != null) {
            block(data)

            if (data.isEmpty()) {
                loadState.setState(LoadState.DONE_FETCHING)
            } else {
                loadState.setState(LoadState.READY_TO_FETCH)
            }
        } else {
            loadState.setState(LoadState.FETCHING_ERROR)
        }
    }

    private fun isInvalid(): Boolean {
        return invalid.get()
    }


    interface LoadCallback<T> {
        fun setResult(data: List<T>?)
    }
}