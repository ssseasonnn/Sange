package zlc.season.sangedemo.demo.normal

import android.arch.lifecycle.MutableLiveData
import zlc.season.sange.DataSource

class NormalDataSource : DataSource<NormalItem>() {
    val refresh = MutableLiveData<Boolean>()
    var page = 0

    override fun loadInitial(loadCallback: LoadCallback<NormalItem>) {
        page = 0
        refresh.postValue(true)

        //loadInitial 将会在子线程中调用, 因此无需担心任何耗时操作
        Thread.sleep(2000)

        // 加载数据
        val items = mutableListOf<NormalItem>()
        for (i in 0 until 10) {
            items.add(NormalItem(i))
        }

        //将加载之后的数据传递给 LoadCallback, 即可轻松更新RecyclerView
        loadCallback.setResult(items)

        refresh.postValue(false)
    }

    override fun loadAfter(loadCallback: LoadCallback<NormalItem>) {
        page++

        //Mock load failed.
        //模拟加载失败.
        if (page % 3 == 0) {
            loadCallback.setResult(null)
            return
        }

        //loadAfter 将会在子线程中调用, 因此无需担心任何耗时操作
        Thread.sleep(2000)

        val items = mutableListOf<NormalItem>()
        for (i in page * 10 until (page + 1) * 10) {
            items.add(NormalItem(i))
        }

        loadCallback.setResult(items)
    }

    override fun onStateChanged(newState: Int) {
        setState(NormalStateItem(state = newState, retry = ::retry))
    }
}