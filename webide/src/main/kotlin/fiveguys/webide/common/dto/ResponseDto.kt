package fiveguys.webide.common.dto

class ResponseDto<T>(
    val status: String,
    val message: String?,
    val data: T?
) {
    companion object {
        fun <T> success(message: String, data: T?): ResponseDto<T> {
            return ResponseDto("Success", message, data)
        }

        fun <T> fail(status: String, message: String?): ResponseDto<T> {
            return ResponseDto(status, message, null)
        }
    }
}
