package zlc.season.sange.datasource

import kotlinx.coroutines.CoroutineScope

abstract class AbstractDataSource<T : Any>(coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {
    private val dataStorage = DataStorage<T>()

    abstract fun notifySubmitList(submitNow: Boolean = false)

    fun totalSize(): Int = dataStorage.totalSize()

    fun toList(): List<T> = dataStorage.toList()

    fun cleanUpAll() = dataStorage.cleanUpAll()

    fun clearAll(delay: Boolean = false) {
        ensureMainThread {
            dataStorage.clearAll()
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun addHeader(t: T, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addHeader(position, t)
            } else {
                dataStorage.addHeader(t)
            }
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun addFooter(t: T, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addFooter(position, t)
            } else {
                dataStorage.addFooter(t)
            }
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


    fun addHeaders(list: List<T>, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addHeaders(position, list)
            } else {
                dataStorage.addHeaders(list)
            }
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun addFooters(list: List<T>, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addFooters(position, list)
            } else {
                dataStorage.addFooters(list)
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


    fun removeHeaderAt(position: Int, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.removeHeaderAt(position)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun removeFooterAt(position: Int, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.removeFooterAt(position)
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


    fun removeHeader(t: T, delay: Boolean = false) {
        ensureMainThread {
            val index = dataStorage.indexHeaderOf(t)
            if (index != -1) {
                dataStorage.removeHeader(t)
                if (!delay) {
                    notifySubmitList()
                }
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }

    fun removeFooter(t: T, delay: Boolean = false) {
        ensureMainThread {
            val index = dataStorage.indexFooterOf(t)
            if (index != -1) {
                dataStorage.removeFooter(t)
                if (!delay) {
                    notifySubmitList()
                }
            } else {
                throw IllegalArgumentException("Wrong index!")
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


    fun setHeader(old: T, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.setHeader(old, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun setFooter(old: T, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.setFooter(old, new)
            if (!delay) {
                notifySubmitList()
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


    fun setHeader(index: Int, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.setHeader(index, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun setFooter(index: Int, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.setFooter(index, new)
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


    fun getHeader(position: Int): T {
        return assertMainThreadWithResult {
            dataStorage.getHeader(position)
        }
    }

    fun getFooter(position: Int): T {
        return assertMainThreadWithResult {
            dataStorage.getFooter(position)
        }
    }

    fun getItem(position: Int): T {
        return assertMainThreadWithResult {
            dataStorage.getItem(position)
        }
    }

    fun getHeaders(): List<T> {
        return assertMainThreadWithResult {
            dataStorage.getHeaders()
        }
    }

    fun getFooters(): List<T> {
        return assertMainThreadWithResult {
            dataStorage.getFooters()
        }
    }

    fun getItems(): List<T> {
        return assertMainThreadWithResult {
            dataStorage.getItems()
        }
    }

    fun clearHeaders(delay: Boolean = false) {
        ensureMainThread {
            dataStorage.clearHeaders()
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun clearFooters(delay: Boolean = false) {
        ensureMainThread {
            dataStorage.clearFooters()
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun clearItems(delay: Boolean = false) {
        ensureMainThread {
            dataStorage.clearItems()
            if (!delay) {
                notifySubmitList()
            }
        }
    }


    fun headerSize(): Int {
        return assertMainThreadWithResult {
            dataStorage.headerSize()
        }
    }

    fun footerSize(): Int {
        return assertMainThreadWithResult {
            dataStorage.footerSize()
        }
    }

    fun itemSize(): Int {
        return assertMainThreadWithResult {
            dataStorage.itemSize()
        }
    }

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
}