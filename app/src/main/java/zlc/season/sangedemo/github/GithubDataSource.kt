package zlc.season.sangedemo.github

import android.arch.lifecycle.MutableLiveData
import zlc.season.sange.DataSource
import zlc.season.sangedemo.api.GithubApi

class GithubDataSource : DataSource<GithubRepositoryResp.GithubRepository>() {
    private val githubApi = GithubApi.create()

    private var searchKey: String = "paging"

    private var page = 0

    var refresh = MutableLiveData<Boolean>()

    override fun loadInitial(loadCallback: LoadCallback<GithubRepositoryResp.GithubRepository>) {
        page = 0
        refresh.postValue(true)

        githubApi.searchRepository("$searchKey+language:kotlin", page)
                .subscribe({
                    loadCallback.setResult(it.items)
                    refresh.postValue(false)
                }, {
                    loadCallback.setResult(null)
                    refresh.postValue(false)
                })
    }

    override fun loadAfter(loadCallback: LoadCallback<GithubRepositoryResp.GithubRepository>) {
        page++

        githubApi.searchRepository("$searchKey+language:kotlin", page)
                .subscribe({
                    loadCallback.setResult(it.items)
                }, {
                    loadCallback.setResult(null)
                })
    }
}