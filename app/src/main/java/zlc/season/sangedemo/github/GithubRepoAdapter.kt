package zlc.season.sangedemo.github

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_hoder_github_repo.*
import zlc.season.sange.DataSource
import zlc.season.sange.PagingAdapter
import zlc.season.sangedemo.R
import zlc.season.sangedemo.github.GithubRepositoryResp.GithubRepository

class GithubRepoAdapter(dataSource: DataSource<GithubRepository>) :
        PagingAdapter<GithubRepository, GithubRepoAdapter.GithubRepoViewHolder>(dataSource) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubRepoViewHolder {
        return GithubRepoViewHolder.create(
                parent,
                R.layout.view_hoder_github_repo
        )
    }

    override fun onBindViewHolder(holder: GithubRepoViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }


    class GithubRepoViewHolder(parent: ViewGroup, res: Int) :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(res, parent, false)),
            LayoutContainer {

        override val containerView: View = itemView

        companion object {
            fun create(vararg params: Any): GithubRepoViewHolder {
                return GithubRepoViewHolder(
                        params[0] as ViewGroup,
                        params[1] as Int
                )
            }
        }

        fun onBind(githubRepository: GithubRepository) {
            tv_title.text = githubRepository.name
            Glide.with(itemView).load(githubRepository.owner.avatar_url).into(iv_user_head)
        }
    }
}