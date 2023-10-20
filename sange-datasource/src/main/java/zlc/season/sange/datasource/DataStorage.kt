package zlc.season.sange.datasource

class DataStorage<T> {
    private val headers = mutableListOf<T>()
    private val items = mutableListOf<T>()
    private val footers = mutableListOf<T>()

    //indicate fetching state
    private var status: T? = null

    fun totalSize(): Int {
        return toList().size
    }

    fun toList(): List<T> {
        val result = mutableListOf<T>()
        result.addAll(headers)
        result.addAll(items)
        result.addAll(footers)

        status?.let {
            result.add(it)
        }

        return result
    }

    fun clearAll() {
        //clean up Item
        items.cleanUp()
        items.clear()

        headers.cleanUp()
        headers.clear()

        footers.cleanUp()
        footers.clear()

        status.cleanUpItem()
        status = null
    }

    fun cleanUpAll() = toList().forEach { it.cleanUpItem() }

    fun addHeader(t: T) = headers.add(t)
    fun addFooter(t: T) = footers.add(t)
    fun addItem(t: T) = items.add(t)

    fun addHeader(position: Int, t: T) = safe { headers.add(position, t) }
    fun addFooter(position: Int, t: T) = safe { footers.add(position, t) }
    fun addItem(position: Int, t: T) = safe { items.add(position, t) }

    fun addHeaders(t: List<T>) = headers.addAll(t)
    fun addFooters(t: List<T>) = footers.addAll(t)
    fun addItems(t: List<T>) = items.addAll(t)

    fun addHeaders(position: Int, t: List<T>) = safe { headers.addAll(position, t) }
    fun addFooters(position: Int, t: List<T>) = safe { footers.addAll(position, t) }
    fun addItems(position: Int, t: List<T>) = safe { items.addAll(position, t) }

    fun removeHeaderAt(position: Int) = safe { headers.removeAt(position).cleanUpItem() }
    fun removeFooterAt(position: Int) = safe { footers.removeAt(position).cleanUpItem() }
    fun removeItemAt(position: Int) = safe { items.removeAt(position).cleanUpItem() }

    fun removeHeader(t: T) = headers.remove(t.apply { cleanUpItem() })
    fun removeFooter(t: T) = footers.remove(t.apply { cleanUpItem() })
    fun removeItem(t: T) = items.remove(t.apply { cleanUpItem() })

    fun indexHeaderOf(t: T) = headers.indexOf(t)
    fun indexFooterOf(t: T) = footers.indexOf(t)
    fun indexItemOf(t: T) = items.indexOf(t)

    fun setHeader(old: T, new: T) = safe { headers[indexHeaderOf(old.apply { cleanUpItem() })] = new }
    fun setFooter(old: T, new: T) = safe { footers[indexFooterOf(old.apply { cleanUpItem() })] = new }
    fun setItem(old: T, new: T) = safe { items[indexItemOf(old.apply { cleanUpItem() })] = new }

    fun getHeader(position: Int) = headers[position]
    fun getFooter(position: Int) = footers[position]
    fun getItem(position: Int) = items[position]

    fun getHeaders() = headers
    fun getFooters() = footers
    fun getItems() = items

    fun headerSize() = headers.size
    fun footerSize() = footers.size
    fun itemSize() = items.size

    fun setHeader(index: Int, new: T) = safe {
        val old = headers[index]
        old.cleanUpItem()
        headers[index] = new
    }

    fun setFooter(index: Int, new: T) = safe {
        val old = footers[index]
        old.cleanUpItem()
        footers[index] = new
    }

    fun setItem(index: Int, new: T) = safe {
        val old = items[index]
        old.cleanUpItem()
        items[index] = new
    }


    fun clearHeaders() {
        headers.cleanUp()
        headers.clear()
    }

    fun clearFooters() {
        //clean up resource before clear
        footers.cleanUp()
        footers.clear()
    }

    fun clearItems() {
        items.cleanUp()
        items.clear()
    }


    fun setState(t: T?) {
        val old = status
        old.cleanUpItem()
        status = t
    }

    fun getState(): T? {
        return status
    }
}