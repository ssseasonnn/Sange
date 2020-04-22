package zlc.season.sange

import androidx.recyclerview.widget.RecyclerView
import android.view.View

open class SangeViewHolder<T>(containerView: View) :
    RecyclerView.ViewHolder(containerView) {

    open fun onBind(t: T) {}

    open fun onBindPayload(t: T, payload: MutableList<Any>) {}

    open fun onAttach(t: T) {}

    open fun onDetach(t: T) {}

    open fun onRecycled(t: T) {}
}