package zlc.season.pagingadapterdemo.zhihu

import zlc.season.paging.MultiDataSource
import zlc.season.pagingadapterdemo.api.ZhihuApi

class DailyDataSource : MultiDataSource<DailyItem>() {
    val api by lazy { ZhihuApi.create() }

    override fun loadInitial(loadCallback: LoadCallback<DailyItem>) {
        super.loadInitial(loadCallback)

        api.getDaily()
                .subscribe({
                    val header = DailyHeaderItem(it.top_stories)
                    this.addHeader(header, delay = true)

                    val items = mutableListOf<DailyItem>()
                    it.stories.forEach {
                        items.add(DailyNormalItem(it))
                    }
                    loadCallback.setResult(items)
                }, {
                    loadCallback.setResult(null)
                })
    }

    override fun loadAfter(loadCallback: LoadCallback<DailyItem>) {
        super.loadAfter(loadCallback)
        loadCallback.setResult(emptyList())
    }
}