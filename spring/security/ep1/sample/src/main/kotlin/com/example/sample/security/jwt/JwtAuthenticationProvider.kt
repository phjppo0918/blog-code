package com.example.sample.security.jwt

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        println("jwt provider 실행")
        val details: UserDetails = userDetailsService.loadUserByUsername(authentication.name)

        return JwtSuccessfulAuthentication(details)
    }

    override fun supports(authentication: Class<*>): Boolean = JwtUnauthenticatedToken::class.java.isAssignableFrom(authentication)
}
