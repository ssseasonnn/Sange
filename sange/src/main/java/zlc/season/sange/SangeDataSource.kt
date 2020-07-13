package zlc.season.sange

open class SangeDataSource<T> : DataSource<T>() {
    override val dataStorage = SangeDataStorage<T>()

    /**
     * Add header
     */
    fun addHeader(t: T, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addHeader(position, t)
            } else {
                dataStorage.addHeader(t)
            }
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun addHeaders(list: List<T>, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addHeaders(position, list)
            } else {
                dataStorage.addHeaders(list)
            }
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun removeHeaderAt(position: Int, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.removeHeaderAt(position)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun removeHeader(t: T, delay: Boolean = false) {
        ensureMainThread {
            val index = dataStorage.indexHeaderOf(t)
            if (index != -1) {
                dataStorage.removeHeader(t)
                if (!delay) {
                    notifySubmitList()
                }
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }

    fun setHeader(old: T, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.setHeader(old, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun setHeader(index: Int, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.setHeader(index, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun getHeader(position: Int): T {
        return assertMainThreadWithResult {
            dataStorage.getHeader(position)
        }
    }

    fun getHeaders(): List<T> {
        return assertMainThreadWithResult {
            dataStorage.getHeaders()
        }
    }

    /**
     * Clear headers
     */
    fun clearHeader(delay: Boolean = false) {
        ensureMainThread {
            dataStorage.clearHeader()
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    /**
     * Return header size
     */
    fun headerSize(): Int {
        return assertMainThreadWithResult {
            dataStorage.headerSize()
        }
    }

    /**
     * Add footer
     */
    fun addFooter(t: T, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addFooter(position, t)
            } else {
                dataStorage.addFooter(t)
            }
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun addFooters(list: List<T>, position: Int = -1, delay: Boolean = false) {
        ensureMainThread {
            if (position > -1) {
                dataStorage.addFooters(position, list)
            } else {
                dataStorage.addFooters(list)
            }
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun removeFooterAt(position: Int, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.removeFooterAt(position)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun removeFooter(t: T, delay: Boolean = false) {
        ensureMainThread {
            val index = dataStorage.indexFooterOf(t)
            if (index != -1) {
                dataStorage.removeFooter(t)
                if (!delay) {
                    notifySubmitList()
                }
            } else {
                throw IllegalArgumentException("Wrong index!")
            }
        }
    }

    fun setFooter(old: T, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.setFooter(old, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun setFooter(index: Int, new: T, delay: Boolean = false) {
        ensureMainThread {
            dataStorage.setFooter(index, new)
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun getFooter(position: Int): T {
        return assertMainThreadWithResult {
            dataStorage.getFooter(position)
        }
    }

    fun getFooters(): List<T> {
        return assertMainThreadWithResult {
            dataStorage.getFooters()
        }
    }

    fun clearFooter(delay: Boolean = false) {
        ensureMainThread {
            dataStorage.clearFooter()
            if (!delay) {
                notifySubmitList()
            }
        }
    }

    fun footerSize(): Int {
        return assertMainThreadWithResult {
            dataStorage.footerSize()
        }
    }
}