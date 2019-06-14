package zlc.season.paging

interface Storage<T> {
    fun all(): List<T>

    fun clear()

    fun addAll(t: List<T>)

    fun addAll(position: Int, t: List<T>)

    fun add(t: T)

    fun add(position: Int, t: T)

    fun removeAt(position: Int)

    fun remove(t: T)

    fun indexOf(t: T): Int
}