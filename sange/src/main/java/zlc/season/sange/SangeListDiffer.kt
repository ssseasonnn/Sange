package zlc.season.sange

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class SangeListDiffer<T>(private val coroutineScope: CoroutineScope) {
    var adapter: RecyclerView.Adapter<*>? = null

    private val diffCallback = DiffCallback<T>()

    private var list = emptyList<T>()
    private var currentList = emptyList<T>()

    // Max generation of currently scheduled runnable
    private var maxScheduledGeneration: Int = 0

    internal fun size() = currentList.size

    internal fun get(position: Int) = currentList[position]

    @SuppressLint("NotifyDataSetChanged")
    internal fun submitList(
        newList: List<T>,
        initial: Boolean = false,
        submitNow: Boolean = false,
    ) {
        if (initial) {
            list = newList
            currentList = Collections.unmodifiableList(newList)
            adapter?.notifyDataSetChanged()
            return
        }

        if (newList === list) {
            // nothing to do
            return
        }

        // incrementing generation means any currently-running diffs are discarded when they finish
        val runGeneration = ++maxScheduledGeneration

        // initial simple removeItem toList
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

        if (submitNow) {
            val result = calcDiffResult(oldList, newList)
            if (maxScheduledGeneration == runGeneration) {
                latchList(newList, result)
            }
        } else {
            coroutineScope.launchIo {
                val result = calcDiffResult(oldList, newList)
                withContext(Dispatchers.Main) {
                    if (maxScheduledGeneration == runGeneration) {
                        latchList(newList, result)
                    }
                }
            }
        }
    }

    private fun calcDiffResult(oldList: List<T>, newList: List<T>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
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

            override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int,
            ): Boolean {
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
    }

    private fun latchList(newList: List<T>, diffResult: DiffUtil.DiffResult) {
        list = newList
        // notify last, after list is updated
        currentList = Collections.unmodifiableList(newList)
        adapter?.let {
            diffResult.dispatchUpdatesTo(it)
        }
    }

    class DiffCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return if (oldItem is Differ && newItem is Differ) {
                oldItem.areItemsTheSame(newItem)
            } else {
                return oldItem === newItem
            }
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return if (oldItem is Differ && newItem is Differ) {
                oldItem.areContentsTheSame(newItem)
            } else {
                true
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
