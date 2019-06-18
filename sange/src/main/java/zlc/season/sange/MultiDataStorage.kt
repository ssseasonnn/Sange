package zlc.season.sange

open class MultiDataStorage<T> : DataStorage<T>() {
    private val headers = mutableListOf<T>()
    private val footers = mutableListOf<T>()

    override fun all(): List<T> {
        val result = mutableListOf<T>()
        result.addAll(headers)
        result.addAll(super.all())
        result.addAll(footers)
        return result
    }

    override fun clear() {
        super.clear()
        headers.clear()
        footers.clear()
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

    fun setHeader(old: T, new: T) {
        headers[indexOf(old)] = new
    }

    fun setHeader(index: Int, new: T) {
        headers[index] = new
    }

    fun clearHeader() {
        headers.clear()
    }


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

    fun setFooter(old: T, new: T) {
        footers[indexOf(old)] = new
    }

    fun setFooter(index: Int, new: T) {
        footers[index] = new
    }

    fun clearFooter() = footers.clear()
}