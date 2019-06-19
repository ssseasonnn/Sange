package zlc.season.sange

open class DataStorage<T> {
    private val data = mutableListOf<T>()

    open fun all(): List<T> {
        val result = mutableListOf<T>()
        result.addAll(data)
        return result
    }

    open fun clear() {
        data.clear()
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

    fun indexOf(t: T): Int {
        return data.indexOf(t)
    }
}