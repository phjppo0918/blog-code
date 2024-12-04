package com.example.sample.security.jwt

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtSuccessfulAuthentication(
    private val user: UserDetails,
) : Authentication {
    override fun getName(): String = user.username

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = user.authorities

    override fun getCredentials(): Any? = user.password

    override fun getDetails(): Any = user

    override fun getPrincipal(): Any = user

    override fun isAuthenticated(): Boolean = true

    override fun setAuthenticated(isAuthenticated: Boolean) {}
}
