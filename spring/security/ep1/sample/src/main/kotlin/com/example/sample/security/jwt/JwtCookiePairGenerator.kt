package com.example.sample.security.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class JwtCookiePairGenerator(
    private val cookieGenerator: CookieGenerator,
    @Value("\${jwt.access-header-name}") private val accessHeaderName: String,
    @Value("\${jwt.refresh-header-name}") private val refreshHeaderName: String,
    @Value("\${jwt.limit-minute}") private val accessTokenLimitMinute: Long,
    @Value("\${jwt.limit-refresh-days}") private val refreshLimitDays: Long,
) {
    fun getJwtCookiePair(
        accessToken: String,
        refreshToken: String,
    ): JwtCookiePair =
        JwtCookiePair(
            accessTokenCookie =
                cookieGenerator.generate(
                    name = accessHeaderName,
                    value = accessToken,
                    age = Duration.ofMinutes(accessTokenLimitMinute),
                ),
            refreshTokenCookie =
                cookieGenerator.generate(
                    name = refreshHeaderName,
                    value = refreshToken,
                    age = Duration.ofDays(refreshLimitDays),
                ),
        )
}
