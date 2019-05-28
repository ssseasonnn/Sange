package zlc.season.paging

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder

abstract class PagingAdapter<T, VH : ViewHolder>(private val dataSource: DataSource<T>) : Adapter<VH>() {

    init {
        dataSource.setPagingUpdateCallback(object : PagingUpdateCallback {
            override fun onItemInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onItemRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onItemChanged(position: Int, count: Int, payload: Any?) {
                notifyItemRangeChanged(position, count, payload)
            }

            override fun onItemMoved(position: Int, count: Int) {
                notifyItemMoved(position, count)
            }
        })
    }

    fun getItem(position: Int): T {
        dataSource.dispatchLoadAround(position)
        return dataSource.getItem(position)
    }

    fun submitList(list: List<T>) {

    }

    override fun getItemCount(): Int {
        return dataSource.getItemCount()
    }

    interface PagingUpdateCallback {
        fun onItemInserted(position: Int, count: Int)

        fun onItemRemoved(position: Int, count: Int)

        fun onItemChanged(position: Int, count: Int, payload: Any?)

        fun onItemMoved(position: Int, count: Int)
    }

}