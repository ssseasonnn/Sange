package zlc.season.paging

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder

abstract class PagingAdapter<T, VH : ViewHolder>(
        protected open val dataSource: DataSource<T>
) : Adapter<VH>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        dataSource.setAdapter(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        dataSource.setAdapter(null)
    }

    open fun getItem(position: Int): T {
        return dataSource.getItem(position)
    }

    override fun getItemCount(): Int {
        return dataSource.getItemCount()
    }
}