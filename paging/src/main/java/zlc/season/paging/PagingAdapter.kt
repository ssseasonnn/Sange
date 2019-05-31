package zlc.season.paging

import android.support.v7.util.AdapterListUpdateCallback
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder

abstract class PagingAdapter<T, VH : ViewHolder>(private val dataSource: DataSource<T>) : Adapter<VH>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        dataSource.setUpdateCallback(AdapterListUpdateCallback(this))
        dataSource.invalidate()
    }

    fun getItem(position: Int): T {
        return dataSource.getItem(position)
    }

    override fun getItemCount(): Int {
        return dataSource.getItemCount()
    }
}