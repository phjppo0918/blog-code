package com.example.sample.security.emailpassword

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class EmailPasswordAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        println("provider")
        val token: UsernamePasswordAuthenticationToken = authentication as UsernamePasswordAuthenticationToken

        val user = userDetailsService.loadUserByUsername(authentication.name)

        if (user.password != token.credentials) {
            throw BadCredentialsException("Invalid username or password")
        }

        return UsernamePasswordAuthenticationToken(user.username, user.password, user.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean =
        UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
}
