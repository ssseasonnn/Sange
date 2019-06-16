package zlc.season.pagingadapterdemo.github

import android.arch.lifecycle.ViewModel

class GithubRepoSearchViewModel : ViewModel() {
    val dataSource = GithubDataSource()

    val refreshStatus = dataSource.refresh

    fun refresh() {
        dataSource.invalidate()
    }
}