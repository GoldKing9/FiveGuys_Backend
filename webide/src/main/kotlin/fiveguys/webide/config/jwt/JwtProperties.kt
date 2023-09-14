package fiveguys.webide.config.jwt

interface JwtProperties {
    companion object {
        const val ACCESS_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L // 1일
        const val REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L // 7일
        const val REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS = REFRESH_TOKEN_EXPIRE_TIME / 1000 // 7일
        const val TOKEN_PREFIX = "Bearer "
        const val HEADER_STRING = "Authorization"
    }
}
