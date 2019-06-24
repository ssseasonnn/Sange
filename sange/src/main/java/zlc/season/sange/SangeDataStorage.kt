package zlc.season.sange

open class SangeDataStorage<T> : DataStorage<T>() {
    private val headers = mutableListOf<T>()
    private val footers = mutableListOf<T>()

    override fun toList(): List<T> {
        val result = mutableListOf<T>()
        result.addAll(headers)
        result.addAll(items)
        result.addAll(footers)

        status?.let {
            result.add(it)
        }

        return result
    }

    override fun clearAll() {
        super.clearAll()
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
        headers[indexItemOf(old)] = new
    }

    fun setHeader(index: Int, new: T) {
        headers[index] = new
    }

    fun headerSize() = headers.size

    fun clearHeader() = headers.clear()


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
        footers[indexItemOf(old)] = new
    }

    fun setFooter(index: Int, new: T) {
        footers[index] = new
    }

    fun footerSize() = footers.size

    fun clearFooter() = footers.clear()
}