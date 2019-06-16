package zlc.season.pagingadapterdemo.zhihu

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_zhihu_daily_header.view.*
import kotlinx.android.synthetic.main.view_holder_zhihu_daily_item.view.*
import zlc.season.paging.MultiDataSource
import zlc.season.paging.PagingAdapter
import zlc.season.paging.inflate
import zlc.season.pagingadapterdemo.R


class DailyAdapter(dataSource: MultiDataSource<DailyItem>) : PagingAdapter<DailyItem, RecyclerView.ViewHolder>(dataSource) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            return DailyViewHolder(parent, R.layout.view_holder_zhihu_daily_item)
        } else {
            return DailyHeaderViewHolder(parent, R.layout.view_holder_zhihu_daily_header)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DailyHeaderViewHolder) {
            holder.onBind(getItem(position).getTopStorys())
        } else if (holder is DailyViewHolder) {
            holder.onBind(getItem(position).getStory())
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType()
    }
}

class DailyHeaderViewHolder(parent: ViewGroup, res: Int) :
    RecyclerView.ViewHolder(parent.inflate(res)), LayoutContainer {

    override val containerView: View get() = itemView

    fun onBind(item: List<DailyResp.TopStory>) {
        val images = mutableListOf<String>()
        item.forEach {
            images.add(it.image)
        }

        val banner = containerView.banner
        banner.setImageLoader(GlideImageLoader())
        banner.setImages(images)
        banner.start()
    }
}

class DailyViewHolder(parent: ViewGroup, res: Int) :
    RecyclerView.ViewHolder(parent.inflate(res)), LayoutContainer {

    override val containerView: View get() = itemView

    fun onBind(item: DailyResp.Story) {
        containerView.tv_content.text = item.title
    }
}