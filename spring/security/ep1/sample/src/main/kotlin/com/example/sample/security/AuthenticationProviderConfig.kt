package com.example.sample.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration

@Configuration
class AuthenticationProviderConfig(
    private val customProviders: Collection<AuthenticationProvider>,
    private val authenticationConfiguration: AuthenticationConfiguration,
) {
    @Bean
    fun AuthenticationManager(): AuthenticationManager =
        ProviderManager(customProviders.toMutableList(), authenticationConfiguration.authenticationManager)
}
