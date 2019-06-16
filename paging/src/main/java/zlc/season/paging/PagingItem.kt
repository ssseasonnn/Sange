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

interface ViewType {
    fun viewType(): Int {
        return 0
    }
}

interface PagingItem : Differ, ViewType