package com.example.sample.security.jwt

import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CookieGenerator(
    private val domain: String = "localhost",
) {
    fun generate(
        name: String,
        value: String,
        age: Duration,
        path: String = "/",
    ): ResponseCookie =
        ResponseCookie
            .from(name, value)
            .sameSite("None")
            .domain(domain)
            .maxAge(age)
            .path(path)
            .secure(true)
            .httpOnly(true)
            .build()
}
