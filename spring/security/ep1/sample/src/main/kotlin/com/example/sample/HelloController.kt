package com.example.sample

import com.example.sample.security.emailpassword.MemberDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/auth")
    fun getHello(
        @AuthenticationPrincipal user: MemberDetails,
    ) {
        println(user)
    }
}
