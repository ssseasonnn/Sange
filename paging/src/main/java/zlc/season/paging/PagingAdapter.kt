package zlc.season.paging

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter

abstract class PagingAdapter<T, VH : RecyclerView.ViewHolder>(
    protected open val dataSource: DataSource<T>
) : Adapter<VH>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        dataSource.setAdapter(this)
    }

    open fun getItem(position: Int): T {
        return dataSource.getItem(position)
    }

    override fun getItemCount(): Int {
        return dataSource.getItemCount()
    }
}