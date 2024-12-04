package com.example.sample

import com.example.sample.security.AuthUser
import com.example.sample.security.emailpassword.MemberDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/auth")
    fun getHello(
        @AuthUser user: MemberDetails,
    ) {
        println("controller")
        println(user)
    }
}
