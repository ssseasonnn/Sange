package zlc.season.paging

class MultiDataStorage<T> : DataStorage<T>() {
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
        try {
            headers[indexOf(old)] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun setHeader(index: Int, new: T) {
        try {
            headers[index] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
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
        try {
            footers[indexOf(old)] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun setFooter(index: Int, new: T) {
        try {
            footers[index] = new
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}