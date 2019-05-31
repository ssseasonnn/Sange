package zlc.season.paging

class DataStore<T> {
    private val data = mutableListOf<T>()

    fun getItemCount(): Int {
        return data.size
    }

    fun getItem(position: Int): T {
        return data[position]
    }

    fun submitData(newData: List<T>, clear: Boolean = false) {
        if (clear) {
            data.clear()
        }
        data.addAll(newData)
    }

    fun isAfterBoundary(position: Int): Boolean {
        return position == data.lastIndex
    }

    fun isBeforeBoundary(position: Int): Boolean {
        return position == 0
    }
}