package fiveguys.webide.common.error

class GlobalException(errorCode: ErrorCode) : RuntimeException() {
    val errorCode: ErrorCode = errorCode
}
