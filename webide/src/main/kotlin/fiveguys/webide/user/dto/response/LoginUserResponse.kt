package fiveguys.webide.user.dto.response

import fiveguys.webide.config.auth.LoginUser
import fiveguys.webide.user.domain.UserRole

data class LoginUserResponse(
    val userId: Long?,
    val nickname: String,
    val role: UserRole,
    val accessToken: String,
    val refreshToken: String
) {
    constructor(loginUser: LoginUser, accessToken: String, refreshToken: String) : this(
        userId = loginUser.user.id,
        nickname = loginUser.user.nickname,
        role = loginUser.user.role,
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}

