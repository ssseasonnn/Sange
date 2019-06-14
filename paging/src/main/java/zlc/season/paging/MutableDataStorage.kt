package zlc.season.paging

class MutableDataStorage<T>(val storage: Storage<T>) : Storage<T> {
    private val headers = mutableListOf<T>()
    private val footers = mutableListOf<T>()

    override fun all(): List<T> {
        val result = mutableListOf<T>()
        result.addAll(headers)
        result.addAll(storage.all())
        result.addAll(footers)
        return result
    }

    override fun clear() {
        storage.clear()
        clearOf(headers, footers)
    }

    override fun addAll(t: List<T>) {
        storage.addAll(t)
    }

    override fun addAll(position: Int, t: List<T>) {
        storage.addAll(position, t)
    }

    override fun add(t: T) {
        storage.add(t)
    }

    override fun add(position: Int, t: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeAt(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(t: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun indexOf(t: T): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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