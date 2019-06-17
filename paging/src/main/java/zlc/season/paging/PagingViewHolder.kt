package zlc.season.paging

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer

open class PagingViewHolder<T>(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {


    open fun onBind(t: T) {
    }

    open fun onBindPayload(payload: MutableList<Any>) {

    }
}