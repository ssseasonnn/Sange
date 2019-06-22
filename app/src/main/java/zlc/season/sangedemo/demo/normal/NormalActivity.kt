package zlc.season.sangedemo.demo.normal

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_demo.*
import zlc.season.sangedemo.R

class NormalActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProviders.of(this)[NormalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = NormalAdapter(viewModel.dataSource)

        swipe_refresh_layout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refresh.observe(this, Observer {
            if (it == null) return@Observer
            swipe_refresh_layout.isRefreshing = it
        })
    }
}