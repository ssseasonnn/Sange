package zlc.season.sange

fun <T> T?.cleanUpItem() {
    this?.let {
        if (this is Cleanable) {
            this.cleanUp()
        }
    }
}

fun <T> List<T>.cleanUp() {
    this.forEach {
        it.cleanUpItem()
    }
}