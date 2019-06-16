package zlc.season.paging

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer

open class PagingViewHolder<T>(parent: ViewGroup, res: Int) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(res, parent, false)),
        LayoutContainer {

    override val containerView: View = itemView

    open fun onBind(t: T) {
    }

    open fun onBindPayload(payload: MutableList<Any>) {

    }
}