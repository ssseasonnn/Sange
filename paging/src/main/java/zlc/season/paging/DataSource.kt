package zlc.season.paging

open class DataSource<T> {
    private val data = mutableListOf<T>()
    private val loadState = LoadState()

    private var pagingUpdateCallback: PagingAdapter.PagingUpdateCallback? = null

    fun setPagingUpdateCallback(callback: PagingAdapter.PagingUpdateCallback) {
        this.pagingUpdateCallback = callback
    }

    init {
        dispatchLoadInit()
    }

    private fun dispatchLoadInit() {
        ioThread {
            loadInit(LoadCallbackImpl { data, _ ->
                this@DataSource.data.clear()
                this@DataSource.data.addAll(data)

                pagingUpdateCallback?.onItemInserted(0, data.size)
            })
        }
    }

    fun dispatchLoadAround(position: Int) {
        if (!loadState.isReady()) {
            return
        }

        if (isTriggerLoad(position)) {

            loadState.setState(LoadState.FETCHING)

            ioThread {
                loadAfter(LoadCallbackImpl { data, _ ->
                    this@DataSource.data.addAll(data)
                    pagingUpdateCallback?.onItemInserted(getItemCount(), data.size)
                })
            }
        }
    }

    fun invalidate() {
        dispatchLoadInit()
    }

    fun getItemCount(): Int {
        return data.size
    }

    fun getItem(position: Int): T {
        return data[position]
    }

    open fun loadBefore(loadCallback: LoadCallback<T>) {}

    open fun loadInit(loadCallback: LoadCallback<T>) {}

    open fun loadAfter(loadCallback: LoadCallback<T>) {}

    open fun isTriggerLoad(position: Int): Boolean {
        if (position == data.lastIndex) {
            return true
        }
        return false
    }

    inner class LoadCallbackImpl<T>(val block: (List<T>, Boolean) -> Unit) : LoadCallback<T> {
        override fun setResult(data: List<T>?, isError: Boolean) {
            mainThread {
                if (isError) {
                    loadState.setState(LoadState.FETCHING_ERROR)
                } else {
                    if (data != null) {
                        block(data, isError)

                        if (data.isEmpty()) {
                            loadState.setState(LoadState.DONE_FETCHING)
                        } else {
                            loadState.setState(LoadState.READY_TO_FETCH)
                        }
                    }
                }
            }
        }
    }

    interface LoadCallback<T> {
        fun setResult(data: List<T>?, isError: Boolean = false)
    }
}