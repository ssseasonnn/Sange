package zlc.season.sange

interface Differ {
    fun areItemsTheSame(other: Differ): Boolean {
        return this === other
    }

    fun areContentsTheSame(other: Differ): Boolean {
        return true
    }

    fun getChangePayload(other: Differ): Any? {
        return null
    }
}