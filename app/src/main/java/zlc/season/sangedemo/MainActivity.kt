package zlc.season.sangedemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import zlc.season.sangedemo.github.GithubRepoSearchActivity
import zlc.season.sangedemo.zhihu.DailyActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_search_repo.setOnClickListener {
            startActivity(Intent(this, GithubRepoSearchActivity::class.java))
        }

        btn_zhihu_daily.setOnClickListener {
            startActivity(Intent(this, DailyActivity::class.java))
        }
    }
}
