package zlc.season.sangedemo.demo

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import zlc.season.sange.SangeDataSource
import zlc.season.sange.SangeItem

class DemoDataSource : SangeDataSource<SangeItem>() {
    val refresh = MutableLiveData<Boolean>()
    var page = 0

    override suspend fun loadInitial(): List<SangeItem>? {
        page = 0

        refresh.postValue(true)

        delay(1500)
        val headers = mutableListOf<SangeItem>()
        for (i in 0 until 2) {
            headers.add(HeaderItem(i))
        }
        addHeaders(headers)



        delay(2000)
        val footers = mutableListOf<SangeItem>()
        for (i in 0 until 2) {
            footers.add(FooterItem(i))
        }
        addFooters(footers)


        delay(2000)
        val items = mutableListOf<SangeItem>()
        for (i in 0 until 10) {
            items.add(NormalItem(i))
        }

        refresh.postValue(false)

        return items
    }

    override suspend fun loadAfter(): List<SangeItem>? {
        page++

        //Mock load failed.
        //模拟加载失败.
        if (page % 3 == 0) {
            return null
        }

        delay(1500)
        val items = mutableListOf<SangeItem>()
        for (i in page * 10 until (page + 1) * 10) {
            items.add(NormalItem(i))
        }

        return items
    }

    override fun onStateChanged(newState: Int) {
        setState(StateItem(newState, ::retry))
    }
}