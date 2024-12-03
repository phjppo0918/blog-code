package com.example.sample.security.member

import kotlin.random.Random

data class Member(
    val id: Long = Random(1).nextLong(),
    val email: String,
    val nickname: String,
    val password: String? = null,
)
