package zlc.season.sange

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION

abstract class SangeMultiAdapter<T : SangeItem, VH : SangeViewHolder<T>>(dataSource: DataSource<T>) :
    SangeAdapter<T, VH>(dataSource) {

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
        holder.checkPosition {
            onAttach(getItem(it))
        }
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        holder.checkPosition {
            onDetach(getItem(it))
        }
    }

    override fun onViewRecycled(holder: VH) {
        holder.checkPosition {
            onRecycled(getItem(it))
        }
    }

    private fun <VH : RecyclerView.ViewHolder> VH.checkPosition(block: VH.(Int) -> Unit) {
        this.adapterPosition.let {
            if (it != NO_POSITION) {
                this.block(it)
            }
        }
    }
}