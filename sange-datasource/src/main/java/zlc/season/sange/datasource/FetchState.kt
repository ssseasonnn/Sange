package zlc.season.sange.datasource

sealed interface FetchState {
    object ReadyToFetch : FetchState
    object Fetching : FetchState
    object DoneFetching : FetchState
    object FetchingError : FetchState
}

