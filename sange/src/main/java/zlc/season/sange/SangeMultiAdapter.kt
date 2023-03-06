package zlc.season.sange

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION

abstract class SangeMultiAdapter<T : SangeItem, VH : SangeViewHolder<T>>(
    dataSource: DataSource<T>,
    shouldInvalidate: Boolean = true
) : SangeAdapter<T, VH>(dataSource, shouldInvalidate) {

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(position, getItem(position))
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            holder.onBindPayload(position, getItem(position), payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType()
    }

    override fun onViewAttachedToWindow(holder: VH) {
        holder.checkPosition {
            onAttach(it, getItem(it))
        }
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        holder.checkPosition {
            onDetach(it, getItem(it))
        }
    }

    override fun onViewRecycled(holder: VH) {
        holder.checkPosition {
            onRecycled(it, getItem(it))
        }
    }

    private fun <VH : RecyclerView.ViewHolder> VH.checkPosition(block: VH.(Int) -> Unit) {
        this.bindingAdapterPosition.let {
            if (it != NO_POSITION) {
                this.block(it)
            }
        }
    }
}