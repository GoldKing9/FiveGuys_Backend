package fiveguys.webide.user.domain

enum class UserRole(
    val role: String
) {
    USER("사용자"),
    ADMIN("관리자"),
}
