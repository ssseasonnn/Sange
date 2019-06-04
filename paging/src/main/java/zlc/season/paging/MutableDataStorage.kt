package zlc.season.paging

class MutableDataStorage<T> : DataStorage<T>() {
    private val headers = mutableListOf<T>()
    private val footers = mutableListOf<T>()

    private var currentList = emptyList<T>()


    override fun all(): List<T> {
        currentList = listOf(headers, data, footers)
        return currentList
    }

    override fun size(): Int {
        return sizeOf(headers, data, footers)
    }

    override fun get(position: Int): T {
        return currentList[position]
    }

    override fun clear() {
        clearOf(headers, data, footers)
    }

    /**
     * Header functions
     */
    fun addHeader(t: T) = headers.add(t)

    fun addHeader(position: Int, t: T) = headers.add(position, t)

    fun addHeaders(t: List<T>) = headers.addAll(t)

    fun addHeaders(position: Int, t: List<T>) = headers.addAll(position, t)

    fun removeHeaderAt(position: Int) = headers.removeAt(position)

    fun removeHeader(t: T) = headers.remove(t)

    fun indexHeaderOf(t: T) = headers.indexOf(t)


    /**
     * Footer functions
     */
    fun addFooter(t: T) = footers.add(t)

    fun addFooter(position: Int, t: T) = footers.add(position, t)

    fun addFooters(t: List<T>) = footers.addAll(t)

    fun addFooters(position: Int, t: List<T>) = footers.addAll(position, t)

    fun removeFooterAt(position: Int) = footers.removeAt(position)

    fun removeFooter(t: T) = footers.remove(t)

    fun indexFooterOf(t: T) = footers.indexOf(t)
}