package zlc.season.sange.datasource

import kotlinx.coroutines.CoroutineScope

abstract class AbstractDataSource<T : Any>(coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {
    private val dataStorage = DataStorage<T>()

    abstract fun notifySubmitList(submitNow: Boolean = false)

    fun totalSize(): Int = dataStorage.totalSize()

    fun toList(): List<T> = dataStorage.toList()

    fun cleanUpAll() = dataStorage.cleanUpAll()

    fun clearAll(delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.clearAll() }

    fun addHeader(t: T, position: Int = -1, delay: Boolean = false) = ensureMainAndNotify(delay) {
        if (position > -1) {
            dataStorage.addHeader(position, t)
        } else {
            dataStorage.addHeader(t)
        }
    }

    fun addFooter(t: T, position: Int = -1, delay: Boolean = false) = ensureMainAndNotify(delay) {
        if (position > -1) {
            dataStorage.addFooter(position, t)
        } else {
            dataStorage.addFooter(t)
        }
    }

    fun addItem(t: T, position: Int = -1, delay: Boolean = false) = ensureMainAndNotify(delay) {
        if (position > -1) {
            dataStorage.addItem(position, t)
        } else {
            dataStorage.addItem(t)
        }
    }

    fun addHeaders(list: List<T>, position: Int = -1, delay: Boolean = false) = ensureMainAndNotify(delay) {
        if (position > -1) {
            dataStorage.addHeaders(position, list)
        } else {
            dataStorage.addHeaders(list)
        }
    }

    fun addFooters(list: List<T>, position: Int = -1, delay: Boolean = false) = ensureMainAndNotify(delay) {
        if (position > -1) {
            dataStorage.addFooters(position, list)
        } else {
            dataStorage.addFooters(list)
        }
    }

    fun addItems(list: List<T>, position: Int = -1, delay: Boolean = false) = ensureMainAndNotify(delay) {
        if (position > -1) {
            dataStorage.addItems(position, list)
        } else {
            dataStorage.addItems(list)
        }
    }

    fun removeHeader(t: T, delay: Boolean = false) = ensureMainAndNotify(delay) {
        val index = dataStorage.indexHeaderOf(t)
        if (index != -1) {
            dataStorage.removeHeader(t)
        }
    }

    fun removeFooter(t: T, delay: Boolean = false) = ensureMainAndNotify(delay) {
        val index = dataStorage.indexFooterOf(t)
        if (index != -1) {
            dataStorage.removeFooter(t)
        }
    }

    fun removeItem(t: T, delay: Boolean = false) = ensureMainAndNotify(delay) {
        val index = dataStorage.indexItemOf(t)
        if (index != -1) {
            dataStorage.removeItem(t)
        }
    }

    fun removeHeaderAt(position: Int, delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.removeHeaderAt(position) }
    fun removeFooterAt(position: Int, delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.removeFooterAt(position) }
    fun removeItemAt(position: Int, delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.removeItemAt(position) }

    fun setHeader(old: T, new: T, delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.setHeader(old, new) }
    fun setFooter(old: T, new: T, delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.setFooter(old, new) }
    fun setItem(old: T, new: T, delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.setItem(old, new) }

    fun setHeader(index: Int, new: T, delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.setHeader(index, new) }
    fun setFooter(index: Int, new: T, delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.setFooter(index, new) }
    fun setItem(index: Int, new: T, delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.setItem(index, new) }

    fun clearHeaders(delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.clearHeaders() }
    fun clearFooters(delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.clearFooters() }
    fun clearItems(delay: Boolean = false) = ensureMainAndNotify(delay) { dataStorage.clearItems() }

    fun getHeader(position: Int): T = withResultOnMain { dataStorage.getHeader(position) }
    fun getFooter(position: Int): T = withResultOnMain { dataStorage.getFooter(position) }
    fun getItem(position: Int): T = withResultOnMain { dataStorage.getItem(position) }

    fun getHeaders(): List<T> = withResultOnMain { dataStorage.getHeaders() }
    fun getFooters(): List<T> = withResultOnMain { dataStorage.getFooters() }
    fun getItems(): List<T> = withResultOnMain { dataStorage.getItems() }

    fun headerSize() = withResultOnMain { dataStorage.headerSize() }
    fun footerSize() = withResultOnMain { dataStorage.footerSize() }
    fun itemSize() = withResultOnMain { dataStorage.itemSize() }

    fun setState(newState: T?) = ensureMainAndNotify(false) { dataStorage.setState(newState) }

    fun getState(): T? {
        return withResultOnMain {
            dataStorage.getState()
        }
    }

    private fun ensureMainAndNotify(delay: Boolean, block: () -> Unit) = ensureMainThread {
        block()
        if (!delay) {
            notifySubmitList()
        }
    }
}