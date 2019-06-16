package zlc.season.pagingadapterdemo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_github_repo_search.*


class GithubRepoSearchActivity : AppCompatActivity() {

    private lateinit var viewModel: GithubRepoSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repo_search)

        viewModel = ViewModelProviders.of(this).get(GithubRepoSearchViewModel::class.java)

        refresh.setOnRefreshListener {
            viewModel.refresh()
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = GithubRepoAdapter(viewModel.dataSource)

        search_btn.setOnClickListener {
//            viewModel.search(editText.text.toString())
            viewModel.remove()
        }

        viewModel.refresh()

        viewModel.refreshStatus.observe(this, Observer {
            it?.let {
                refresh.isRefreshing = it
            }
        })
    }

}