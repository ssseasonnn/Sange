package zlc.season.paging

import android.support.v7.util.AdapterListUpdateCallback
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter

abstract class PagingAdapter<T, VH : RecyclerView.ViewHolder>(
    protected open val dataSource: DataSource<T>
) : Adapter<VH>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(
            PagingScrollListener(
                onReachStart = {
                    log("reach start")
                    dataSource.dispatchLoadAround(DataSource.Direction.BEFORE)
                }, onReachEnd = {
                    log("reach end")
                    dataSource.dispatchLoadAround(DataSource.Direction.AFTER)
                })
        )
        dataSource.setListCallback(AdapterListUpdateCallback(this))
    }

    open fun getItem(position: Int): T {
        return dataSource.getItem(position)
    }

    override fun getItemCount(): Int {
        return dataSource.getItemCount()
    }
}