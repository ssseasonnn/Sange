package zlc.season.sange

open class DataStorage<T> {
    private val data = mutableListOf<T>()

    open fun all(): List<T> {
        val result = mutableListOf<T>()
        result.addAll(data)
        return result
    }

    open fun clearAll() {
        data.clear()
    }

    fun clear() {
        data.clear()
    }

    fun size(): Int {
        return data.size
    }

    fun addAll(t: List<T>) {
        data.addAll(t)
    }

    fun addAll(position: Int, t: List<T>) {
        data.addAll(position, t)
    }

    fun add(t: T) {
        data.add(t)
    }

    fun add(position: Int, t: T) {
        try {
            data.add(position, t)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun removeAt(position: Int) {
        try {
            data.removeAt(position)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun remove(t: T) {
        data.remove(t)
    }

    fun set(old: T, new: T) {
        try {
            data[indexOf(old)] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun set(index: Int, new: T) {
        try {
            data[index] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun get(position: Int) = data[position]

    fun indexOf(t: T): Int {
        return data.indexOf(t)
    }
}