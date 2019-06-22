package zlc.season.sangedemo.demo.normal

import android.arch.lifecycle.ViewModel

class NormalViewModel : ViewModel() {
    val dataSource = NormalDataSource()

    val refresh = dataSource.refresh


    fun refresh() {
        dataSource.invalidate()
    }
}