package com.example.sample.security

import com.example.sample.security.jwt.AuthenticationRedirectSetUpFilter
import com.example.sample.security.jwt.JwtCheckFilter
import com.example.sample.security.oauth2.OAuth2AuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val authenticationManager: AuthenticationManager,
) {
    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain =
        http
            .authorizeHttpRequests {
                it.requestMatchers("/auth").authenticated()
            }.oauth2Login { l ->
                l.authorizationEndpoint { endpoint ->
                    endpoint.baseUri("/auth/login/oauth2")
                }
                l.successHandler(oAuth2AuthenticationSuccessHandler)
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.csrf { it.disable() }
            .addFilterBefore(AuthenticationRedirectSetUpFilter(), OAuth2AuthorizationRequestRedirectFilter::class.java)
            .addFilterBefore(JwtCheckFilter(authenticationManager), UsernamePasswordAuthenticationFilter::class.java)
            .formLogin {
                it.loginProcessingUrl("/auth/login/email")
                it.usernameParameter("email")
                it.passwordParameter("password")
                it.successHandler(oAuth2AuthenticationSuccessHandler)
            }.httpBasic { it.disable() }
            .build()
}
