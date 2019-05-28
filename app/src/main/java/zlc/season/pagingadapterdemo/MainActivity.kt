package zlc.season.pagingadapterdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TestAdapter(TestDataSource())
    }

    class TestDataSource : zlc.season.paging.DataSource<String>() {
        override fun loadInit(loadCallback: LoadCallback<String>) {
            super.loadInit(loadCallback)
            val data = mutableListOf<String>()
            for (i in 0 until 10) {
                data.add("test $i")
            }
            loadCallback.setResult(data)
        }

        override fun loadNextPage(loadCallback: LoadCallback<String>) {
            super.loadNextPage(loadCallback)

            if (getItemCount() > 30) {
                loadCallback.setResult(null, true)
                return
            }

            val data = mutableListOf<String>()
            for (i in getItemCount() until getItemCount() + 10) {
                data.add("test $i")
            }
            loadCallback.setResult(data)
        }
    }
}
