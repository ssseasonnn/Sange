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

        headers.cleanUp()
        headers.clear()

        footers.cleanUp()
        footers.clear()
    }


    /**
     * Header functions
     */
    fun addHeader(t: T) = headers.add(t)

    fun addHeader(position: Int, t: T) = headers.add(position, t)

    fun addHeaders(t: List<T>) = headers.addAll(t)

    fun addHeaders(position: Int, t: List<T>) = headers.addAll(position, t)

    fun removeHeaderAt(position: Int) {
        val removed = headers.removeAt(position)
        removed.cleanUpItem()
    }

    fun removeHeader(t: T) {
        t.cleanUpItem()
        headers.remove(t)
    }

    fun indexHeaderOf(t: T) = headers.indexOf(t)

    fun setHeader(old: T, new: T) {
        old.cleanUpItem()
        headers[indexHeaderOf(old)] = new
    }

    fun getHeader(position: Int) = headers[position]

    fun getHeaders() = headers

    fun setHeader(index: Int, new: T) {
        val old = headers[index]
        old.cleanUpItem()
        headers[index] = new
    }

    fun headerSize() = headers.size

    fun clearHeader() {
        headers.cleanUp()
        headers.clear()
    }


    /**
     * Footer functions
     */
    fun addFooter(t: T) = footers.add(t)

    fun addFooter(position: Int, t: T) = footers.add(position, t)

    fun addFooters(t: List<T>) = footers.addAll(t)

    fun addFooters(position: Int, t: List<T>) = footers.addAll(position, t)

    fun removeFooterAt(position: Int) {
        val removed = footers.removeAt(position)
        removed.cleanUpItem()
    }

    fun removeFooter(t: T) {
        t.cleanUpItem()
        footers.remove(t)
    }

    fun indexFooterOf(t: T) = footers.indexOf(t)

    fun setFooter(old: T, new: T) {
        old.cleanUpItem()
        footers[indexFooterOf(old)] = new
    }

    fun setFooter(index: Int, new: T) {
        //clean up resource before set new footer
        val old = footers[index]
        old.cleanUpItem()

        footers[index] = new
    }

    fun getFooter(position: Int) = footers[position]

    fun getFooters() = footers

    fun footerSize() = footers.size

    fun clearFooter() {
        //clean up resource before clear
        footers.cleanUp()
        footers.clear()
    }
}