package zlc.season.sangedemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import zlc.season.sangedemo.databinding.ActivityMainBinding
import zlc.season.sangedemo.demo.DemoActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnNormal.setOnClickListener {
            startActivity(Intent(this, DemoActivity::class.java))
        }
    }
}