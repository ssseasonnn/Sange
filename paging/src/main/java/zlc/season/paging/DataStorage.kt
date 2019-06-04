package zlc.season.paging

open class DataStorage<T> {
    protected val data = mutableListOf<T>()

    open fun all() = data.toList()

    open fun size() = data.size

    open fun get(position: Int) = data[position]

    open fun clear() = data.clear()

    fun addAll(t: List<T>) = data.addAll(t)

    fun addAll(position: Int, t: List<T>) = data.addAll(position, t)

    fun add(t: T) = data.add(t)

    fun add(position: Int, t: T) = data.add(position, t)

    fun removeAt(position: Int) = data.removeAt(position)

    fun remove(t: T) = data.remove(t)

    fun indexOf(t: T) = data.indexOf(t)
}