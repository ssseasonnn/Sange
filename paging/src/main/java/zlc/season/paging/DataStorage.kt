package zlc.season.paging

open class DataStorage<T> : Storage<T> {
    protected val data = mutableListOf<T>()

    override fun all(): List<T> {
        return data.toList()
    }

    override fun clear() = data.clear()

    override fun addAll(t: List<T>) {
        data.addAll(t)
    }

    override fun addAll(position: Int, t: List<T>) {
        data.addAll(position, t)
    }

    override fun add(t: T) {
        data.add(t)
    }

    override fun add(position: Int, t: T) = data.add(position, t)

    override fun removeAt(position: Int) {
        data.removeAt(position)
    }

    override fun remove(t: T) {
        data.remove(t)
    }

    override fun indexOf(t: T): Int {
        return data.indexOf(t)
    }
}