package fiveguys.webide.common.aop

object ThreadLocalContextHolder {
    private val traceIdHolder = ThreadLocal<String>()
    fun setTraceId(traceId: String) {
        traceIdHolder.set(traceId)
    }

    fun getTraceId(): String? {
        return traceIdHolder.get()
    }

    fun clear() {
        traceIdHolder.remove()
    }
}
