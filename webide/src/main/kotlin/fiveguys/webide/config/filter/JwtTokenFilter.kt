package fiveguys.webide.config.filter

import fiveguys.webide.config.jwt.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtTokenFilter(private val jwtUtils: JwtUtils): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = jwtUtils.parseJwtToken(request)
        if (token != null && jwtUtils.validationJwtToken(token,response)) {
            val loginUser = jwtUtils.verify(token)
            val authentication = UsernamePasswordAuthenticationToken(loginUser, null, loginUser.authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}
