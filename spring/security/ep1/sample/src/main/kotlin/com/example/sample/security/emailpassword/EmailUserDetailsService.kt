package com.example.sample.security.emailpassword

import com.example.sample.security.member.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class EmailUserDetailsService(
    private val memberRepository: MemberRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        println("userDetailsService 실행")
        return MemberDetails.from(memberRepository.finById(username.toLong())!!)
    }
}
