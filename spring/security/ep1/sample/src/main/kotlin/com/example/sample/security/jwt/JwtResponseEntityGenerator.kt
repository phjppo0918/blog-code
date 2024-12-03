package com.example.sample.security.jwt

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class JwtResponseEntityGenerator(
    private val jwtCookiePairGenerator: JwtCookiePairGenerator,
) {
    fun getResponseBuilder(
        accessToken: String,
        refreshToken: String,
    ): ResponseEntity.BodyBuilder {
        val cookiePair: JwtCookiePair =
            jwtCookiePairGenerator.getJwtCookiePair(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )

        return ResponseEntity
            .ok()
            .header(
                HttpHeaders.SET_COOKIE,
                cookiePair.accessTokenCookie.toString(),
            ).header(
                HttpHeaders.SET_COOKIE,
                cookiePair.refreshTokenCookie.toString(),
            )
    }
}
