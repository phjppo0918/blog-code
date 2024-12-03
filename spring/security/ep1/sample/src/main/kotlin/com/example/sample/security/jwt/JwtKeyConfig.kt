package com.example.sample.security.jwt

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.Key

@Configuration
class JwtKeyConfig {
    @Bean
    fun key(
        @Value("\${jwt.key}") jwtKey: String,
    ): Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey))
}
