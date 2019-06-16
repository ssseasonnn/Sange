package zlc.season.paging

abstract class MultiPagingAdapter<T : PagingItem, VH : PagingViewHolder<T>>(dataSource: DataSource<T>) :
    PagingAdapter<T, VH>(dataSource) {

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            holder.onBindPayload(payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType()
    }
}