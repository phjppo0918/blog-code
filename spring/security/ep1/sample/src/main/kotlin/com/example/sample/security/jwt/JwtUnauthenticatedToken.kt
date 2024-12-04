package com.example.sample.security.jwt

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtUnauthenticatedToken(
    private val token: String,
) : Authentication {
    override fun getName(): String = token

    override fun getAuthorities(): Collection<out GrantedAuthority> = emptyList()

    override fun getCredentials(): Any? = null

    override fun getDetails(): Any? = null

    override fun getPrincipal(): Any? = null

    override fun isAuthenticated(): Boolean = false

    override fun setAuthenticated(isAuthenticated: Boolean) {}
}
