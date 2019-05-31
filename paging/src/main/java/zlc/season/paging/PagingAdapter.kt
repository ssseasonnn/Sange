package zlc.season.paging

import android.support.v7.util.AdapterListUpdateCallback
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder

abstract class PagingAdapter<T, VH : ViewHolder>(private val dataSourceFactory: DataSource.Factory<T>) : Adapter<VH>() {
    private var dataSource = dataSourceFactory.create()

    fun currentDataSource(): DataSource<T> {
        return dataSource
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        dataSource.setUpdateCallback(AdapterListUpdateCallback(this))
        dataSource.invalidate()

        dataSource.setInvalidateCallback {

        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

    }

    fun getItem(position: Int): T {
        return dataSource.getItem(position)
    }

    override fun getItemCount(): Int {
        return dataSource.getItemCount()
    }
}