package fiveguys.webide.user.service

import fiveguys.webide.config.jwt.JwtUtils
import fiveguys.webide.user.domain.UserRepository
import fiveguys.webide.user.dto.request.LoginRequest
import fiveguys.webide.user.dto.request.SignupRequest
import fiveguys.webide.user.dto.response.LoginUserResponse
import fiveguys.webide.common.error.ErrorCode
import fiveguys.webide.common.error.GlobalException
import fiveguys.webide.config.auth.LoginUser
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils,
) {

    @Transactional
    fun signup(request: SignupRequest) {
        if (!request.isValid) {
            throw GlobalException(ErrorCode.RETRY_EMAIL_AUTH)
        }
        val user = request.toEntity(bCryptPasswordEncoder.encode(request.password))
        userRepository.save(user)
    }

    @Transactional
    fun login(request: LoginRequest): LoginUserResponse {
        val authenticationToken = UsernamePasswordAuthenticationToken(request.email, request.password)
        val authentication = authenticationManager.authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        val loginUser = authentication.principal as LoginUser
        val accessToken = jwtUtils.generateAccessTokenFromLoginUser(loginUser)
        return LoginUserResponse(
            loginUser,
            accessToken,
            "refreshToken"
        )
    }
}
