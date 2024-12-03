package com.example.sample.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.time.ZonedDateTime
import java.util.Date

@Component
class AccessTokenGenerator(
    private val key: Key,
    @Value("\${jwt.limit-minute}") private val limitMinute: Long,
) {
    fun generate(token: TokenClaims): String {
        val claims = mapToClaims(token)
        return compact(claims)
    }

    private fun mapToClaims(claim: TokenClaims): Claims =
        Jwts
            .claims()
            .add("id", claim.id)
            .add("role", claim.role)
            .build()

    private fun compact(claims: Claims): String {
        val now = ZonedDateTime.now()

        return Jwts
            .builder()
            .claims(claims)
            .issuedAt(Date.from(now.toInstant()))
            .signWith(key)
            .expiration(Date.from(now.plusMinutes(limitMinute).toInstant()))
            .compact()
    }
}
