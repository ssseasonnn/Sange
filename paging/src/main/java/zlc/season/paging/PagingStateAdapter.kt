package zlc.season.paging

import android.view.ViewGroup

class PagingStateAdapter(dataSource: DataSource<PagingItem>) :
    PagingAdapter<PagingItem, PagingViewHolder<PagingItem>>(dataSource) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder<PagingItem> {
        return PagingViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: PagingViewHolder<PagingItem>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType()
    }
}