package zlc.season.sange.datasource

class FetchStateHolder {
    private var fetchState: FetchState = FetchState.ReadyToFetch

    fun isNotReady(): Boolean {
        return fetchState != FetchState.ReadyToFetch
    }

    fun setState(state: FetchState) {
        this.fetchState = state
    }

    fun getState(): FetchState {
        return fetchState
    }
}