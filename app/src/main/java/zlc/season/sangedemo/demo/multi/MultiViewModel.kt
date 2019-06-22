package zlc.season.sangedemo.demo.multi

import android.arch.lifecycle.ViewModel

class MultiViewModel : ViewModel() {
    val dataSource = MultiDataSource()

    val refresh = dataSource.refresh


    fun refresh() {
        dataSource.invalidate()
    }
}