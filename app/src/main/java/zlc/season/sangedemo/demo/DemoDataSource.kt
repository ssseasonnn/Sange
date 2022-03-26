package zlc.season.sangedemo.demo

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import zlc.season.sange.SangeDataSource
import zlc.season.sange.SangeItem

class DemoDataSource(coroutineScope: CoroutineScope) : SangeDataSource<SangeItem>(coroutineScope) {
    val refresh = MutableLiveData<Boolean>()
    var page = 0

    override suspend fun loadInitial(): List<SangeItem>? {
        println("load init")
        page = 0

        refresh.value = true

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
        for (i in 0 until 2) {
            items.add(NormalItem(i))
        }

        refresh.value = false

        return items
    }

    override suspend fun loadAfter(): List<SangeItem>? {
        println("load after")
        page++

        //Mock load failed.
        //模拟加载失败.
        if (page % 3 == 0) {
            return null
        }

        delay(1500)
        val items = mutableListOf<SangeItem>()
        for (i in page * 2 until (page + 1) * 2) {
            items.add(NormalItem(i))
        }

        return items
    }

    override fun onStateChanged(newState: Int) {
        setState(StateItem(newState, ::retry))
    }
}