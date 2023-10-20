package zlc.season.sange.datasource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

abstract class DataSource<T : Any>(coroutineScope: CoroutineScope = MainScope()) : AbstractDataSource<T>(coroutineScope) {
    private val fetchState = FetchState()
    private val invalid = AtomicBoolean(false)
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

    private fun dispatchLoadInitial(clear: Boolean) {
        if (clear) {
            clearAll(delay = true)
        }

        launch {
            val result = loadInitial()
            onLoadResult(result)

            invalid.compareAndSet(true, false)

            if (result == null) {
                retryFunc = { dispatchLoadInitial(clear) }
            }

            onLoadInitialFinished(getFetchState())
        }
    }

    protected fun dispatchLoadAround(position: Int) {
        if (isInvalid()) return

        if (shouldLoadNext(position)) {
            if (fetchState.isNotReady()) {
                return
            }
            scheduleLoadAfter()
        }
    }

    private fun scheduleLoadAfter() {
        changeFetchState(FetchState.FETCHING)

        launch {
            val result = loadAfter()
            if (isInvalid()) return@launch

            onLoadResult(result)

            if (result == null) {
                retryFunc = { scheduleLoadAfter() }
            }

            onLoadAfterFinished(getFetchState())
        }
    }

    private fun onLoadResult(data: List<T>?) {
        if (data != null) {
            if (data.isEmpty()) {
                changeFetchState(FetchState.DONE_FETCHING)
            } else {
                addItems(data, delay = true)
                notifySubmitList()

                changeFetchState(FetchState.READY_TO_FETCH)
            }
        } else {
            changeFetchState(FetchState.FETCHING_ERROR)
        }
    }

    private fun isInvalid(): Boolean {
        return invalid.get()
    }

    private fun changeFetchState(newState: Int) {
        fetchState.setState(newState)
        onFetchStateChanged(newState)
    }

    open suspend fun loadInitial(): List<T>? = emptyList()

    open suspend fun loadAfter(): List<T>? = emptyList()

    open fun shouldLoadNext(position: Int): Boolean {
        return position == totalSize() - 1
    }

    fun getFetchState() = fetchState.getState()

    /**
     * Call on state changed.
     * @param newState May be these values:
     *  [FetchState.FETCHING], [FetchState.FETCHING_ERROR],
     *  [FetchState.DONE_FETCHING], [FetchState.READY_TO_FETCH]
     */
    protected open fun onFetchStateChanged(newState: Int) {}

    /**
     * Call on load initial finish
     */
    protected open fun onLoadInitialFinished(currentState: Int) {}

    /**
     * Call on load after finish
     */
    protected open fun onLoadAfterFinished(currentState: Int) {}
}