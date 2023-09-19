package fiveguys.webide.common.error

class GlobalException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message) {
}
