package zlc.season.sangedemo.demo.multi

import android.view.View
import kotlinx.android.synthetic.main.view_holder_footer.*
import kotlinx.android.synthetic.main.view_holder_header.*
import kotlinx.android.synthetic.main.view_holder_normal.*
import kotlinx.android.synthetic.main.view_holder_state.*
import zlc.season.sange.FetchingState
import zlc.season.sange.SangeItem
import zlc.season.sange.SangeViewHolder

class NormalViewHolder(containerView: View) :
        SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(t: SangeItem) {
        super.onBind(t)
        t as NormalItem
        tv_normal_content.text = t.toString()
    }
}

class HeaderViewHolder(containerView: View) :
        SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(t: SangeItem) {
        super.onBind(t)
        t as HeaderItem
        tv_header_content.text = t.toString()
    }
}

class FooterViewHolder(containerView: View) :
        SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(t: SangeItem) {
        super.onBind(t)
        t as FooterItem
        tv_footer_content.text = t.toString()
    }
}

class StateViewHolder(containerView: View) :
        SangeViewHolder<SangeItem>(containerView) {

    override fun onBind(t: SangeItem) {
        super.onBind(t)
        t as StateItem

        tv_state_content.setOnClickListener {
            t.retry()
        }

        when {
            t.state == FetchingState.FETCHING -> {
                state_loading.visibility = View.VISIBLE
                tv_state_content.visibility = View.GONE
            }
            t.state == FetchingState.FETCHING_ERROR -> {
                state_loading.visibility = View.GONE
                tv_state_content.visibility = View.VISIBLE
            }
            t.state == FetchingState.DONE_FETCHING -> {
                state_loading.visibility = View.GONE
                tv_state_content.visibility = View.GONE
            }
            else -> {
                state_loading.visibility = View.GONE
                tv_state_content.visibility = View.GONE
            }
        }
    }
}