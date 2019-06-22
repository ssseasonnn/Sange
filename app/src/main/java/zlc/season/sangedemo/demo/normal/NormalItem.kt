package zlc.season.sangedemo.demo.normal

open class NormalItem(val number: Int)

class NormalStateItem(
    val state: Int,
    val retry: () -> Unit
) : NormalItem(-1)