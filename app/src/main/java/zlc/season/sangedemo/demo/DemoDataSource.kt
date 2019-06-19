package zlc.season.sangedemo.demo

import android.arch.lifecycle.MutableLiveData
import zlc.season.sange.MultiDataSource
import zlc.season.sange.SangeItem

class DemoDataSource : MultiDataSource<SangeItem>() {
    val refresh = MutableLiveData<Boolean>()
    var page = 0

    override fun loadInitial(loadCallback: LoadCallback<SangeItem>) {
        page = 0

        refresh.postValue(true)

        Thread.sleep(1500)

        val headers = mutableListOf<SangeItem>()
        for (i in 0 until 2) {
            headers.add(HeaderItem(i))
        }

        val items = mutableListOf<SangeItem>()
        for (i in 0 until 10) {
            items.add(NormalItem(i))
        }

        val footers = mutableListOf<SangeItem>()
        for (i in 0 until 2) {
            footers.add(FooterItem(i))
        }

        addHeaders(headers, delay = true)
        addFooters(footers, delay = true)

        loadCallback.setResult(items)

        refresh.postValue(false)
    }

    override fun loadAfter(loadCallback: LoadCallback<SangeItem>) {
        page++

        //Mock load failed.
        //模拟加载失败.
        if (page % 3 == 0) {
            loadCallback.setResult(null)
            return
        }

        Thread.sleep(1500)
        val items = mutableListOf<SangeItem>()
        for (i in page * 10 until (page + 1) * 10) {
            items.add(NormalItem(i))
        }

        loadCallback.setResult(items)
    }

    override fun onStateChanged(newState: Int) {
        super.onStateChanged(newState)
        setState(StateItem(newState, ::retry))
    }
}