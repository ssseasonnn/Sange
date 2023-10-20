package zlc.season.sange.datasource

class FetchState {
    companion object {
        const val READY_TO_FETCH = 0
        const val FETCHING = 1
        const val DONE_FETCHING = 2
        const val FETCHING_ERROR = 3
    }

    private var fetchState = READY_TO_FETCH

    fun isNotReady(): Boolean {
        return fetchState != READY_TO_FETCH
    }

    fun setState(state: Int) {
        this.fetchState = state
    }

    fun getState(): Int {
        return fetchState
    }
}