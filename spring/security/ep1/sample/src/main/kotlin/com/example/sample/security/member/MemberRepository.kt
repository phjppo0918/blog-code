package com.example.sample.security.member

import org.springframework.stereotype.Repository

@Repository
class MemberRepository {
    val storage = mutableListOf<Member>(Member(1, "1", "1"))

    fun add(member: Member) {
        storage.add(member)
    }

    fun findByEmail(email: String): Member? = storage.find { it.email == email }

    fun finById(id: Long): Member? = storage.find { it.id == id }
}
