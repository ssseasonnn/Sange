package zlc.season.sangedemo.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import zlc.season.sangedemo.R
import zlc.season.sangedemo.databinding.ActivityDemoBinding

class DemoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDemoBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[DemoViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val adapter = DemoAdapter(viewModel.dataSource)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refresh.observe(this, Observer {
            if (it == null) return@Observer
            binding.swipeRefreshLayout.isRefreshing = it
        })
    }
}