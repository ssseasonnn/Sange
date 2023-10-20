package zlc.season.sange

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class SangeAdapter<T : Any, VH : ViewHolder>(
    private val dataSource: SangeDataSource<T>,
    private val shouldInvalidate: Boolean
) : Adapter<VH>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        dataSource.setAdapter(this, shouldInvalidate)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        dataSource.setAdapter(null, false)
    }

    open fun getItem(position: Int): T {
        return dataSource.getItemForAdapter(position)
    }

    override fun getItemCount(): Int {
        return dataSource.getSizeForAdapter()
    }
}