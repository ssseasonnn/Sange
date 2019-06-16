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

    var i = -1
    var j = 0

    fun remove() {
//        dataSource.removeAt(0)

        val old = dataSource.get(j++)
        i--
        val new = old.copy(id = i, name = "id $i")
        dataSource.set(old, new)
    }

}