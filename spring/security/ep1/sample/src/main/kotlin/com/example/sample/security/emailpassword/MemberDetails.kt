package com.example.sample.security.emailpassword

import com.example.sample.security.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class MemberDetails(
    private val id: Long,
    private val email: String,
    private val nickname: String,
    private val password: String?,
) : UserDetails {
    companion object {
        fun from(member: Member): MemberDetails =
            MemberDetails(
                id = member.id,
                email = member.email,
                nickname = member.nickname,
                password = member.password,
            )
    }

    override fun getAuthorities(): Collection<out GrantedAuthority> =
        listOf(
            SimpleGrantedAuthority("ROLE_USER"),
        )

    override fun getPassword(): String? = password

    override fun getUsername(): String = email
}
