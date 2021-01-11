package zlc.season.sange

import androidx.recyclerview.widget.RecyclerView
import android.view.View

open class SangeViewHolder<T>(containerView: View) :
    RecyclerView.ViewHolder(containerView) {

    open fun onBind(position: Int, t: T) {}

    open fun onBindPayload(position: Int, t: T, payload: MutableList<Any>) {}

    open fun onAttach(position: Int, t: T) {}

    open fun onDetach(position: Int, t: T) {}

    open fun onRecycled(position: Int, t: T) {}
}