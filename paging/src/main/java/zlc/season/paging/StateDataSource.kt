package zlc.season.paging

class StateDataSource : DataSource<PagingItem>() {

    override fun onCreateStorage(): Storage<PagingItem> {
        return StateDataStorage(MutableDataStorage(DataStorage()))
    }

    private fun getStorage(): StateDataStorage {
        return onCreateStorage() as StateDataStorage
    }

    override fun setFetchingState(direction: Direction, newState: Int) {
        super.setFetchingState(direction, newState)
        if (direction == Direction.AFTER) {
            getStorage().afterState = StatePagingItem(newState)
        } else {
            getStorage().beforeState = StatePagingItem(newState)
        }
        notifySubmitList()
    }
}