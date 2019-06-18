package zlc.season.sange

abstract class MultiPagingAdapter<T : PagingItem, VH : PagingViewHolder<T>>(dataSource: DataSource<T>) :
    PagingAdapter<T, VH>(dataSource) {

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            holder.onBindPayload(getItem(position), payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType()
    }

    override fun onViewAttachedToWindow(holder: VH) {
        holder.onAttach(getItem(holder.adapterPosition))
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        holder.onDetach(getItem(holder.adapterPosition))
    }

    override fun onViewRecycled(holder: VH) {
        holder.onRecycled(getItem(holder.adapterPosition))
    }
}