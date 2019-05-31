package zlc.season.paging

class LoadState {
    companion object {
        val READY_TO_FETCH = 0
        val FETCHING = 1
        val DONE_FETCHING = 2
        val FETCHING_ERROR = 3
    }

    private var loadState = READY_TO_FETCH

    fun isNotReady(): Boolean {
        return loadState != READY_TO_FETCH
    }

    fun setState(state: Int) {
        this.loadState = state
    }
}