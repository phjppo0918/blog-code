package com.example.sample.security.oauth2

import com.example.sample.security.member.MemberRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2AuthenticationSuccessHandler(
    private val memberRepository: MemberRepository,
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        println("====================== oauth2 authentication success")

        val coo =
            ResponseCookie
                .from("Authorization", "swad")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build()
                .toString()

        println(request.getHeader("host"))

        response.addHeader(HttpHeaders.SET_COOKIE, coo)
        response.sendRedirect(request.session.getAttribute("client-redirect-url").toString())
        request.session.removeAttribute("client-redirect-url")
    }
}
