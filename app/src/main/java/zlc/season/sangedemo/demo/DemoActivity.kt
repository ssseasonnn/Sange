package zlc.season.sangedemo.demo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_demo.*
import zlc.season.sangedemo.R

class DemoActivity : AppCompatActivity() {
    private val demoViewModel by lazy {
        ViewModelProviders.of(this)[DemoViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = DemoAdapter(demoViewModel.dataSource)

        swipe_refresh_layout.setOnRefreshListener {
            demoViewModel.refresh()
        }

        demoViewModel.refresh.observe(this, Observer {
            if (it == null) return@Observer
            swipe_refresh_layout.isRefreshing = it
        })
    }
}