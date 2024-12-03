package com.example.sample.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtCheckFilter(
    private val accessTokenKey: String,
    private val refreshTokenKey: String,
    private val accessTokenDecoder: AccessTokenDecoder,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        var accessToken: String? = getAccessToken(request)

        accessToken?.run {
            val claims: TokenClaims = accessTokenDecoder.decode(this)
            SecurityContextHolder.getContext().authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun getAccessToken(request: HttpServletRequest): String? =
        request.cookies
            ?.filter { it.name == accessTokenKey }
            ?.map { it.value }
            ?.firstOrNull()
}
