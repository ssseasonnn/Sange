package zlc.season.paging

import android.support.v7.widget.RecyclerView.ViewHolder

abstract class PagingStateAdapter<T, VH : ViewHolder>(dataSource: DataSource<T>) :
    PagingAdapter<T, VH>(dataSource) {


}