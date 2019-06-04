package zlc.season.pagingadapterdemo

import android.arch.lifecycle.MutableLiveData
import zlc.season.paging.DataSource
import zlc.season.pagingadapterdemo.api.GithubApi

class GithubDataSource : DataSource<GithubRepositoryResp.GithubRepository>() {
    private val githubApi = GithubApi.create()

    private var searchKey: String = "paging"

    private var page = 0

    var refresh = MutableLiveData<Boolean>()

    fun setSearchKey(key: String) {
        this.searchKey = key
    }

    override fun loadInitial(loadCallback: LoadCallback<GithubRepositoryResp.GithubRepository>) {
        page = 0
        refresh.postValue(true)

        Thread.sleep(2000)
        val result = mutableListOf<GithubRepositoryResp.GithubRepository>()
        for (i in 0 until 10) {
            result.add(GithubRepositoryResp.GithubRepository(id = i, name = "id $i"))
        }
        loadCallback.setResult(result)
        refresh.postValue(false)

//        githubApi.searchRepository("$searchKey+language:kotlin", page)
//                .subscribe({
//                    loadCallback.setResult(it.items)
//                    refresh.postValue(false)
//                }, {
//                    loadCallback.setResult(null)
//                    refresh.postValue(false)
//                })
    }

    override fun loadAfter(loadCallback: LoadCallback<GithubRepositoryResp.GithubRepository>) {

        page++

        Thread.sleep(2000)
        val result = mutableListOf<GithubRepositoryResp.GithubRepository>()
        for (i in page * 10 until page * 10 + 10) {
            result.add(GithubRepositoryResp.GithubRepository(id = i, name = "id $i"))
        }
        loadCallback.setResult(result)

//        githubApi.searchRepository("$searchKey+language:kotlin", page)
//                .subscribe({
//                    loadCallback.setResult(it.items)
//                }, {
//                    loadCallback.setResult(null)
//                })
    }
}