package com.example.sample.security.jwt

import org.springframework.http.ResponseCookie

data class JwtCookiePair(
    val accessTokenCookie: ResponseCookie,
    val refreshTokenCookie: ResponseCookie,
)
