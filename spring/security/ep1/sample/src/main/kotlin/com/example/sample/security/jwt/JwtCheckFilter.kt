package com.example.sample.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtCheckFilter(
    private val authenticationManager: AuthenticationManager,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        println("jwt check filter 실행")
        SecurityContextHolder.getContext().authentication =
            authenticationManager.authenticate(JwtUnauthenticatedToken(request.getHeader("Authorization")))

        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val header = request.getHeader("Authorization")

        return header == null && request.servletPath.startsWith("/login")
    }
}
