package zlc.season.paging

interface Differ {
    fun areItemsTheSame(other: Differ): Boolean {
        return this === other
    }

    fun areContentsTheSame(other: Differ): Boolean {
        return this == other
    }

    fun getChangePayload(other: Differ): Any? {
        return null
    }
}