package fiveguys.webide.common.error

class GlobalException(val errorCode: ErrorCode) : RuntimeException() {
    override val message: String
        get() = errorCode.message
}
