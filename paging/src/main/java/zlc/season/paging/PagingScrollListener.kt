package zlc.season.paging

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.NO_POSITION
import android.support.v7.widget.StaggeredGridLayoutManager

class PagingScrollListener(
    val onReachStart: () -> Unit,
    val onReachEnd: () -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (isReachStart(recyclerView)) {
            onReachStart()
        }

        if (isReachEnd(recyclerView)) {
            onReachEnd()
        }
    }

    private fun isReachStart(recyclerView: RecyclerView): Boolean {
        val layoutManager = recyclerView.layoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = getFirstVisibleItemPosition(layoutManager)

        return visibleItemCount in 1..totalItemCount &&
                firstVisibleItemPosition == 0
    }

    private fun isReachEnd(recyclerView: RecyclerView): Boolean {
        val layoutManager = recyclerView.layoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = getLastVisibleItemPosition(layoutManager)

        return visibleItemCount in 1..totalItemCount &&
                lastVisibleItemPosition == totalItemCount - 1

    }

    private fun getLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager): Int {
        return when (layoutManager) {
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is StaggeredGridLayoutManager -> {
                val positions = IntArray(layoutManager.spanCount)
                layoutManager.findLastVisibleItemPositions(positions)
                positions.max() ?: NO_POSITION
            }
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> throw IllegalStateException("Unsupported layout manager!")
        }
    }

    private fun getFirstVisibleItemPosition(layoutManager: RecyclerView.LayoutManager): Int {
        return when (layoutManager) {
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is StaggeredGridLayoutManager -> {
                val positions = IntArray(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(positions)
                positions.min() ?: NO_POSITION
            }
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> throw IllegalStateException("Unsupported layout manager!")
        }
    }
}