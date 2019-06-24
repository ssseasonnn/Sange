package zlc.season.sangedemo.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import zlc.season.sange.DataSource
import zlc.season.sange.SangeItem
import zlc.season.sange.SangeMultiAdapter
import zlc.season.sange.SangeViewHolder
import zlc.season.sangedemo.R

class DemoAdapter(dataSource: DataSource<SangeItem>) :
        SangeMultiAdapter<SangeItem, SangeViewHolder<SangeItem>>(dataSource) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SangeViewHolder<SangeItem> {
        return when (viewType) {
            NORMAL -> NormalViewHolder(
                inflate(
                    parent,
                    R.layout.view_holder_normal
                )
            )
            HEADER -> HeaderViewHolder(
                inflate(
                    parent,
                    R.layout.view_holder_header
                )
            )
            FOOTER -> FooterViewHolder(
                inflate(
                    parent,
                    R.layout.view_holder_footer
                )
            )
            STATE -> StateViewHolder(
                inflate(
                    parent,
                    R.layout.view_holder_state
                )
            )
            else -> throw  IllegalStateException("not support this view type:[$viewType]")
        }
    }

    private fun inflate(parent: ViewGroup, res: Int): View {
        return LayoutInflater.from(parent.context).inflate(res, parent, false)
    }
}