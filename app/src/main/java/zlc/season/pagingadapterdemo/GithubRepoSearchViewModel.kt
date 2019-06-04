package zlc.season.pagingadapterdemo

import android.arch.lifecycle.ViewModel

class GithubRepoSearchViewModel : ViewModel() {
    val dataSource = GithubDataSource()

    val refreshStatus = dataSource.refresh

    fun search(key: String) {
        dataSource.setSearchKey(key)
        dataSource.invalidate()
    }

    fun refresh() {
        dataSource.invalidate()
    }

}