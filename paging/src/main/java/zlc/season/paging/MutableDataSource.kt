package zlc.season.paging

open class MutableDataSource<T> : DataSource<T>() {
    override val dataStorage = MutableDataStorage<T>()

    fun clear() {
        assertMainThread {
            dataStorage.clear()
            notifySubmitList()
        }
    }

    /**
     * Data functions
     */
    fun add(t: T, position: Int = -1) {
        assertMainThread {
            if (position > -1) {
                dataStorage.add(position, t)
            } else {
                dataStorage.add(t)
            }

            notifySubmitList()
        }
    }

    fun addAll(list: List<T>, position: Int = -1) {
        assertMainThread {
            if (position > -1) {
                dataStorage.addAll(position, list)
            } else {
                dataStorage.addAll(list)
            }
            notifySubmitList()
        }
    }

    fun removeAt(position: Int) {
        assertMainThread {
            dataStorage.removeAt(position)
            notifySubmitList()
        }
    }

    fun remove(t: T) {
        assertMainThread {
            val index = dataStorage.indexOf(t)
            if (index != -1) {
                dataStorage.remove(t)
                notifySubmitList()
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }

    /**
     * Header functions
     */
    fun addHeader(t: T, position: Int = -1) {
        assertMainThread {
            if (position > -1) {
                dataStorage.addHeader(position, t)
            } else {
                dataStorage.addHeader(t)
            }
            notifySubmitList()
        }
    }

    fun addAllHeaders(list: List<T>, position: Int = -1) {
        assertMainThread {
            if (position > -1) {
                dataStorage.addHeaders(position, list)
            } else {
                dataStorage.addHeaders(list)
            }
            notifySubmitList()
        }
    }

    fun removeHeaderAt(position: Int) {
        assertMainThread {
            dataStorage.removeHeaderAt(position)
            notifySubmitList()
        }
    }

    fun removeHeader(t: T) {
        assertMainThread {
            val index = dataStorage.indexHeaderOf(t)
            if (index != -1) {
                dataStorage.removeHeader(t)
                notifySubmitList()
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }

    /**
     * Footer functions
     */
    fun addFooter(t: T, position: Int = -1) {
        assertMainThread {
            if (position > -1) {
                dataStorage.addFooter(position, t)
            } else {
                dataStorage.addFooter(t)
            }
            notifySubmitList()
        }
    }

    fun addAllFooters(list: List<T>, position: Int = -1) {
        assertMainThread {
            if (position > -1) {
                dataStorage.addFooters(position, list)
            } else {
                dataStorage.addFooters(list)
            }
            notifySubmitList()
        }
    }

    fun removeFooterAt(position: Int) {
        assertMainThread {
            dataStorage.removeFooterAt(position)
            notifySubmitList()
        }
    }

    fun removeFooter(t: T) {
        assertMainThread {
            val index = dataStorage.indexFooterOf(t)
            if (index != -1) {
                dataStorage.removeFooter(t)
                notifySubmitList()
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }
}