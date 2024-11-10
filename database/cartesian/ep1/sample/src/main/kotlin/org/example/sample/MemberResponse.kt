package org.example.sample

data class MemberResponse(
    val id: Long,
    val name: String,
    val roles: Collection<String>,
    val teams: Collection<String>,
)