package zlc.season.pagingadapterdemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_hoder_github_repo.view.*
import zlc.season.paging.DataSource
import zlc.season.paging.PagingAdapter
import zlc.season.pagingadapterdemo.GithubRepositoryResp.GithubRepository

fun ViewGroup.inflate(res: Int, attach: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(res, this, attach)
}

class GithubRepoAdapter(dataSource: DataSource<GithubRepository>) :
        PagingAdapter<GithubRepository, GithubRepoAdapter.GithubRepoViewHolder>(dataSource) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubRepoViewHolder {
        return GithubRepoViewHolder.create(parent, R.layout.view_hoder_github_repo)
    }

    override fun onBindViewHolder(holder: GithubRepoViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }


    class GithubRepoViewHolder : RecyclerView.ViewHolder {

        constructor(parent: ViewGroup, res: Int) : super(parent.inflate(res))

        companion object {
            fun create(vararg params: Any): GithubRepoViewHolder {
                return GithubRepoViewHolder(params[0] as ViewGroup, params[1] as Int)
            }
        }

        fun onBind(githubRepository: GithubRepository) {
            itemView.tv_title.text = githubRepository.name
        }
    }
}