package zlc.season.paging

import android.annotation.SuppressLint
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import zlc.season.ironbranch.ioThread
import zlc.season.ironbranch.mainThread
import java.util.*

class PagingListDiffer<T> {
    var adapter: RecyclerView.Adapter<*>? = null

    private val diffCallback = PagingDiffCallback<T>()

    private var list = emptyList<T>()
    private var currentList = emptyList<T>()

    fun size() = currentList.size

    fun get(position: Int) = currentList[position]

    // Max generation of currently scheduled runnable
    private var mMaxScheduledGeneration: Int = 0


    fun submitList(newList: List<T>, initial: Boolean = false) {
        if (initial) {
            list = newList
            currentList = Collections.unmodifiableList(newList)
            adapter?.notifyDataSetChanged()
        }

        if (newList === list) {
            // nothing to do
            return
        }

        // incrementing generation means any currently-running diffs are discarded when they finish
        val runGeneration = ++mMaxScheduledGeneration

        // initial simple remove all
        if (newList.isEmpty()) {

            val countRemoved = list.size
            list = emptyList()
            currentList = emptyList()
            // notify last, after list is updated
            adapter?.notifyItemRangeRemoved(0, countRemoved)
            return
        }

        // initial simple first insert
        if (list.isEmpty()) {
            list = newList
            currentList = Collections.unmodifiableList(newList)
            adapter?.notifyItemRangeInserted(0, newList.size)
            return
        }

        val oldList = list

        ioThread {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return oldList.size
                }

                override fun getNewListSize(): Int {
                    return newList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return diffCallback.areItemsTheSame(
                            oldList[oldItemPosition], newList[newItemPosition]
                    )
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return diffCallback.areContentsTheSame(
                            oldList[oldItemPosition], newList[newItemPosition]
                    )
                }

                override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                    return diffCallback.getChangePayload(
                            oldList[oldItemPosition], newList[newItemPosition]
                    )
                }
            })

            mainThread {
                if (mMaxScheduledGeneration == runGeneration) {
                    latchList(newList, result)
                }
            }
        }
    }

    private fun latchList(newList: List<T>, diffResult: DiffUtil.DiffResult) {
        list = newList
        // notify last, after list is updated
        currentList = Collections.unmodifiableList(newList)
        adapter?.let {
            diffResult.dispatchUpdatesTo(adapter)
        }
    }

    class PagingDiffCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return if (oldItem is Differ && newItem is Differ) {
                oldItem.areItemsTheSame(newItem)
            } else {
                oldItem === newItem
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return if (oldItem is Differ && newItem is Differ) {
                oldItem.areContentsTheSame(newItem)
            } else {
                oldItem == newItem
            }
        }

        override fun getChangePayload(oldItem: T, newItem: T): Any? {
            return if (oldItem is Differ && newItem is Differ) {
                oldItem.getChangePayload(newItem)
            } else {
                null
            }
        }
    }
}
