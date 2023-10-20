package zlc.season.sange

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import zlc.season.sange.datasource.DataSource
import zlc.season.sange.datasource.ensureMainThread
import zlc.season.sange.datasource.mainScope

open class SangeDataSource<T : Any>(coroutineScope: CoroutineScope = mainScope) : DataSource<T>(coroutineScope) {
    private val pagingListDiffer = SangeListDiffer<T>(coroutineScope)

    override fun notifySubmitList(submitNow: Boolean) {
        ensureMainThread {
            pagingListDiffer.submitList(toList(), submitNow = submitNow)
        }
    }

    // internal function for adapter
    internal fun setAdapter(adapter: RecyclerView.Adapter<*>?, shouldInvalidate: Boolean) {
        pagingListDiffer.adapter = adapter
        if (adapter != null) {
            if (shouldInvalidate) {
                invalidate(false)
            } else {
                notifySubmitList()
            }
        }
    }

    open fun getSizeForAdapter(): Int {
        return pagingListDiffer.size()
    }

    open fun getItemForAdapter(position: Int): T {
        dispatchLoadAround(position)
        return pagingListDiffer.get(position)
    }
}