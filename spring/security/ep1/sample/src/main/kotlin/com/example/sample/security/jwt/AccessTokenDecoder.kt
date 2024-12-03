package com.example.sample.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.security.Key
import javax.crypto.SecretKey

@Component
class AccessTokenDecoder(
    private val key: Key,
) {
    fun decode(token: String): TokenClaims {
        val claims: Claims = parseClaims(token)
        val attributes: Map<String, Any> =
            claims.mapValues { it.value }

        return TokenClaims.from(attributes)
    }

    private fun parseClaims(token: String): Claims =
        Jwts
            .parser()
            .verifyWith(key as SecretKey)
            .build()
            .parseSignedClaims(token)
            .payload
}
