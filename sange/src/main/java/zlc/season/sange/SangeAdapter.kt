package zlc.season.sange

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.View

abstract class SangeAdapter<T : Any, VH : ViewHolder>(
    protected open val dataSource: DataSource<T>
) : Adapter<VH>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnAttachStateChangeListener(
            object : View.OnAttachStateChangeListener {
                override fun onViewDetachedFromWindow(v: View?) {
                    recyclerView.adapter = null
                    dataSource.setAdapter(null)
                    recyclerView.removeOnAttachStateChangeListener(this)
                    if (hasObservers()) {
                        Log.d("Sange", "has observers")
                    } else {
                        Log.d("Sange", "no observers")
                        dataSource.close()
                    }
                }

                override fun onViewAttachedToWindow(v: View?) {
                }
            })
        dataSource.setAdapter(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        dataSource.setAdapter(null)
    }

    open fun getItem(position: Int): T {
        return dataSource.getItemInner(position)
    }

    override fun getItemCount(): Int {
        return dataSource.getItemCount()
    }
}