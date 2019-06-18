package zlc.season.sangedemo.zhihu

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_zhihu_daily.*
import zlc.season.sangedemo.R

class DailyActivity : AppCompatActivity() {
    val dataSource by lazy { DailyDataSource() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhihu_daily)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = DailyAdapter(dataSource)

        dataSource.invalidate()
    }
}