package zlc.season.sangedemo.demo.normal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_holder_normal.view.tv_normal_content
import kotlinx.android.synthetic.main.view_holder_normal_with_state.view.*
import zlc.season.sange.DataSource
import zlc.season.sange.FetchingState
import zlc.season.sange.SangeAdapter
import zlc.season.sangedemo.R

class NormalAdapter(dataSource: DataSource<NormalItem>) :
        SangeAdapter<NormalItem, NormalAdapter.NormalViewHolder>(dataSource) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalViewHolder {
        return NormalViewHolder(inflate(parent, R.layout.view_holder_normal_with_state))
    }

    override fun onBindViewHolder(holder: NormalViewHolder, position: Int) {
        val item = getItem(position)
        if (item is NormalStateItem) {
            holder.onBindState(item)
        } else {
            holder.onBind(item)
        }
    }

    class NormalViewHolder(containerView: View) :
            RecyclerView.ViewHolder(containerView) {

        fun onBind(t: NormalItem) {
            itemView.tv_normal_content.visibility = View.VISIBLE
            itemView.tv_normal_content.text = "item ${t.number}"

            itemView.tv_state_content.visibility = View.GONE
            itemView.state_loading.visibility = View.GONE
        }

        fun onBindState(t: NormalStateItem) {
            itemView.tv_normal_content.visibility = View.GONE
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

    private fun inflate(parent: ViewGroup, res: Int): View {
        return LayoutInflater.from(parent.context).inflate(res, parent, false)
    }
}