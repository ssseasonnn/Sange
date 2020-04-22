package zlc.season.sangedemo.demo

import android.view.View
import kotlinx.android.synthetic.main.view_holder_footer.view.*
import kotlinx.android.synthetic.main.view_holder_header.view.*
import kotlinx.android.synthetic.main.view_holder_normal.view.*
import kotlinx.android.synthetic.main.view_holder_state.view.*
import zlc.season.sange.FetchingState
import zlc.season.sange.SangeItem
import zlc.season.sange.SangeViewHolder

class NormalViewHolder(containerView: View) :
    SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(t: SangeItem) {
        super.onBind(t)
        t as NormalItem
        itemView.tv_normal_content.text = t.toString()
    }
}

class HeaderViewHolder(containerView: View) :
    SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(t: SangeItem) {
        super.onBind(t)
        t as HeaderItem
        itemView.tv_header_content.text = t.toString()
    }
}

class FooterViewHolder(containerView: View) :
    SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(t: SangeItem) {
        super.onBind(t)
        t as FooterItem
        itemView.tv_footer_content.text = t.toString()
    }
}

class StateViewHolder(containerView: View) :
    SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(t: SangeItem) {
        super.onBind(t)
        t as StateItem

        itemView.tv_state_content.setOnClickListener {
            t.retry()
        }

        when {
            t.state == FetchingState.FETCHING -> {
                itemView.state_loading.visibility = View.VISIBLE
                itemView.tv_state_content.visibility = View.GONE
            }
            t.state == FetchingState.FETCHING_ERROR -> {
                itemView.state_loading.visibility = View.GONE
                itemView.tv_state_content.visibility = View.VISIBLE
            }
            t.state == FetchingState.DONE_FETCHING -> {
                itemView.state_loading.visibility = View.GONE
                itemView.tv_state_content.visibility = View.GONE
            }
            else -> {
                itemView.state_loading.visibility = View.GONE
                itemView.tv_state_content.visibility = View.GONE
            }
        }
    }
}