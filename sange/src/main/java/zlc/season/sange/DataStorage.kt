package zlc.season.sange

open class DataStorage<T> {
    private val data = mutableListOf<T>()

    open fun all(): List<T> {
        return data.toList()
    }

    open fun clear() = data.clear()

    open fun addAll(t: List<T>) {
        data.addAll(t)
    }

    open fun addAll(position: Int, t: List<T>) {
        data.addAll(position, t)
    }

    open fun add(t: T) {
        data.add(t)
    }

    open fun add(position: Int, t: T) {
        try {
            data.add(position, t)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    open fun removeAt(position: Int) {
        try {
            data.removeAt(position)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    open fun remove(t: T) {
        data.remove(t)
    }

    open fun set(old: T, new: T) {
        try {
            data[indexOf(old)] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    open fun set(index: Int, new: T) {
        try {
            data[index] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    open fun indexOf(t: T): Int {
        return data.indexOf(t)
    }
}