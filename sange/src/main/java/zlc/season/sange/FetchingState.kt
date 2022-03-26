package zlc.season.sange

class FetchingState {
    companion object {
        const val READY_TO_FETCH = 0
        const val FETCHING = 1
        const val DONE_FETCHING = 2
        const val FETCHING_ERROR = 3
    }

    private var loadState = READY_TO_FETCH

    fun isNotReady(): Boolean {
        return loadState != READY_TO_FETCH
    }

    fun setState(state: Int) {
        this.loadState = state
    }

    fun getState(): Int {
        return loadState
    }
}