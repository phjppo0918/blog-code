package org.example.sample.data

data class MemberTotalData(
    val id: Long,
    val name: String,
    val memberRoleId: Long?,
    val memberRoleName: String?,
    val memberTeamId: Long?,
    val memberTeamName: String?,
)