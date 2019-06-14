package zlc.season.paging

class StateDataStorage(private val storage: Storage<PagingItem>) : Storage<PagingItem> {

    var beforeState: PagingItem? = null
    var afterState: PagingItem? = null

    override fun all(): List<PagingItem> {
        val result = mutableListOf<PagingItem>()
        beforeState?.let { result.add(it) }
        result.addAll(storage.all())
        afterState?.let { result.add(it) }
        return result
    }

    override fun clear() {
        storage.clear()
    }

    override fun addAll(t: List<PagingItem>) {
        storage.addAll(t)
    }

    override fun addAll(position: Int, t: List<PagingItem>) {
        storage.addAll(position, t)
    }
}