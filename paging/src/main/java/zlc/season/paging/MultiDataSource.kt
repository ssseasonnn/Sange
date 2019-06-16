package zlc.season.paging

import zlc.season.ironbranch.ioThread
import zlc.season.ironbranch.mainThread

open class MultiDataSource<T> : DataSource<T>() {

    override val dataStorage: MultiDataStorage<T> = MultiDataStorage()

    interface MultiLoadCallback<T> : LoadCallback<T> {
        fun setHeaderResult(header: List<T>?)
    }

    override fun dispatchLoadInitial() {
        log("load initial start")
        ioThread {
            loadInitial(object : MultiLoadCallback<T> {
                override fun setHeaderResult(header: List<T>?) {
                    mainThread {
                        if (header != null) {
                            dataStorage.addHeaders(header)
                        }
                    }
                }

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

    override fun loadInitial(loadCallback: LoadCallback<T>) {
        super.loadInitial(loadCallback)
    }

    /**
     * Header functions
     */
    fun addHeader(t: T, position: Int = -1) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addHeader(position, t)
            } else {
                dataStorage.addHeader(t)
            }
            notifySubmitList()
        }
    }

    fun addAllHeaders(list: List<T>, position: Int = -1) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addHeaders(position, list)
            } else {
                dataStorage.addHeaders(list)
            }
            notifySubmitList()
        }
    }

    fun removeHeaderAt(position: Int) {
        ensureMainThread {
            dataStorage.removeHeaderAt(position)
            notifySubmitList()
        }
    }

    fun removeHeader(t: T) {
        ensureMainThread {
            val index = dataStorage.indexHeaderOf(t)
            if (index != -1) {
                dataStorage.removeHeader(t)
                notifySubmitList()
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }

    fun setHeader(old: T, new: T) {
        ensureMainThread {
            dataStorage.setHeader(old, new)
            notifySubmitList()
        }
    }

    fun setHeader(index: Int, new: T) {
        ensureMainThread {
            dataStorage.setHeader(index, new)
            notifySubmitList()
        }
    }

    /**
     * Footer functions
     */
    fun addFooter(t: T, position: Int = -1) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addFooter(position, t)
            } else {
                dataStorage.addFooter(t)
            }
            notifySubmitList()
        }
    }

    fun addAllFooters(list: List<T>, position: Int = -1) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addFooters(position, list)
            } else {
                dataStorage.addFooters(list)
            }
            notifySubmitList()
        }
    }

    fun removeFooterAt(position: Int) {
        ensureMainThread {
            dataStorage.removeFooterAt(position)
            notifySubmitList()
        }
    }

    fun removeFooter(t: T) {
        ensureMainThread {
            val index = dataStorage.indexFooterOf(t)
            if (index != -1) {
                dataStorage.removeFooter(t)
                notifySubmitList()
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }

    fun setFooter(old: T, new: T) {
        ensureMainThread {
            dataStorage.setFooter(old, new)
            notifySubmitList()
        }
    }

    fun setFooter(index: Int, new: T) {
        ensureMainThread {
            dataStorage.setFooter(index, new)
            notifySubmitList()
        }
    }
}