package zlc.season.pagingadapterdemo

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_holder_test.view.*

class TestAdapter(dataSource: zlc.season.paging.DataSource<String>) : zlc.season.paging.PagingAdapter<String, TestAdapter.TestViewHolder>(dataSource) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {

        Log.d("Adapter", "on create view holder")

        return TestViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_holder_test,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: TestViewHolder, position: Int) {
        Log.d("Adapter", "on bind view holder")

        val item = getItem(position)
        viewHolder.onBind(item)
    }

    class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(str: String) {
            itemView.tv_content.text = str
        }
    }
}