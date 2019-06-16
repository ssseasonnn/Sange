package zlc.season.pagingadapterdemo.zhihu

import zlc.season.paging.MultiDataSource
import zlc.season.paging.log
import zlc.season.pagingadapterdemo.api.ZhihuApi

class DailyDataSource : MultiDataSource<DailyItem>() {
    val api by lazy { ZhihuApi.create() }

    override fun loadInitial(loadCallback: LoadCallback<DailyItem>) {
        super.loadInitial(loadCallback)

        api.getDaily()
                .subscribe({
                    val header = DailyHeaderItem(it.top_stories)
                    addHeader(header)

                    val items = mutableListOf<DailyItem>()
                    it.stories.forEach {
                        items.add(DailyNormalItem(it))
                    }
                    loadCallback.setResult(items)
                }, {
                    it.message?.let { it1 -> log(it1) }
                })
    }

    override fun loadAfter(loadCallback: LoadCallback<DailyItem>) {
        super.loadAfter(loadCallback)
    }
}