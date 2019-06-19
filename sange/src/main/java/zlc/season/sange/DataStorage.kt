package zlc.season.sange

open class DataStorage<T> {
    protected val items = mutableListOf<T>()
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
        items.clear()
        status = null
    }

    fun clearItem() {
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
            items.removeAt(position)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun removeItem(t: T) {
        items.remove(t)
    }

    fun setItem(old: T, new: T) {
        try {
            items[indexItemOf(old)] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun setItem(index: Int, new: T) {
        try {
            items[index] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun getItem(position: Int) = items[position]

    fun indexItemOf(t: T): Int {
        return items.indexOf(t)
    }

    /**
     *  Set state
     */
    fun setState(t: T?) {
        status = t
    }

    fun getState(): T? {
        return status
    }
}