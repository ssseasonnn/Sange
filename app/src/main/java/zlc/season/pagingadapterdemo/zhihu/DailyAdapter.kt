package zlc.season.pagingadapterdemo.zhihu

import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_holder_zhihu_daily_header.*
import kotlinx.android.synthetic.main.view_holder_zhihu_daily_item.view.*
import zlc.season.paging.MultiDataSource
import zlc.season.paging.MultiPagingAdapter
import zlc.season.paging.PagingViewHolder
import zlc.season.pagingadapterdemo.R


class DailyAdapter(dataSource: MultiDataSource<DailyItem>) :
    MultiPagingAdapter<DailyItem, PagingViewHolder<DailyItem>>(dataSource) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder<DailyItem> {
        if (viewType == 0) {
            return DailyNormalViewHolder(parent, R.layout.view_holder_zhihu_daily_item)
        } else {
            return DailyHeaderViewHolder(parent, R.layout.view_holder_zhihu_daily_header)
        }
    }
}


class DailyHeaderViewHolder(parent: ViewGroup, res: Int) :
    PagingViewHolder<DailyItem>(parent, res) {

    override fun onBind(t: DailyItem) {
        super.onBind(t)
        val item = t.getTopStorys()
        val images = mutableListOf<String>()
        item.forEach {
            images.add(it.image)
        }

        id_banner.setImageLoader(GlideImageLoader())
        id_banner.setImages(images)
        id_banner.start()
    }
}

class DailyNormalViewHolder(parent: ViewGroup, res: Int) :
    PagingViewHolder<DailyItem>(parent, res) {

    override fun onBind(t: DailyItem) {
        super.onBind(t)
        val item = t.getStory()
        containerView.tv_content.text = item.title
    }
}