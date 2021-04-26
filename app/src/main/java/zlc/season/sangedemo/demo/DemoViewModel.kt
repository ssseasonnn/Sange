package zlc.season.sangedemo.demo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class DemoViewModel : ViewModel(), CoroutineScope by MainScope() {
    val dataSource = DemoDataSource(this)

    val refresh = dataSource.refresh


    fun refresh() {
        dataSource.invalidate()
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}