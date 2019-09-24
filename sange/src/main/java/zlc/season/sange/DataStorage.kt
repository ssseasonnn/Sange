package zlc.season.sange

open class DataStorage<T> {
    protected val items = mutableListOf<T>()

    //indicate fetching state
    protected var status: T? = null

    open fun toList(): List<T> {
        val result = mutableListOf<T>()
        result.addAll(items)

        status?.let {
            result.add(it)
        }

        return result
    }

    open fun clearAll() {
        //clean up Item
        items.cleanUp()
        items.clear()

        status.cleanUpItem()
        status = null
    }

    fun clearItem() {
        items.cleanUp()
        items.clear()
    }

    fun itemSize(): Int {
        return items.size
    }

    fun addItems(t: List<T>) {
        items.addAll(t)
    }

    fun addItems(position: Int, t: List<T>) {
        items.addAll(position, t)
    }

    fun addItem(t: T) {
        items.add(t)
    }

    fun addItem(position: Int, t: T) {
        try {
            items.add(position, t)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun removeItemAt(position: Int) {
        try {
            val removed = items.removeAt(position)
            removed.cleanUpItem()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun removeItem(t: T) {
        t.cleanUpItem()
        items.remove(t)
    }

    fun setItem(old: T, new: T) {
        try {
            old.cleanUpItem()
            items[indexItemOf(old)] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun setItem(index: Int, new: T) {
        try {
            val old = items[index]
            old.cleanUpItem()
            items[index] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun getItem(position: Int) = items[position]

    fun getItemList() = items

    fun indexItemOf(t: T): Int {
        return items.indexOf(t)
    }

    /**
     *  Set state
     */
    fun setState(t: T?) {
        val old = status
        old.cleanUpItem()
        status = t
    }

    fun getState(): T? {
        return status
    }


}