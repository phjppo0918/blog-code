package org.example.sample

import org.apache.ibatis.annotations.Mapper
import org.example.sample.data.MemberData
import org.example.sample.data.MemberTotalData
import org.example.sample.data.RoleData
import org.example.sample.data.TeamData


@Mapper
interface SampleRepository {
    fun selectTotalLeftJoin(): Collection<MemberTotalData>
    fun selectMember(): Collection<MemberData>
    fun selectRole(): Collection<RoleData>
    fun selectTeam(): Collection<TeamData>
    fun selectTotalInnerJoin(): Collection<MemberTotalData>

    fun selectTotalLeftJoinTop10(): Collection<MemberTotalData>
    fun selectMemberTop10(): Collection<MemberData>
    fun selectRoleTop10Member(): Collection<RoleData>
    fun selectTeamTop10Member(): Collection<TeamData>
    fun selectTotalInnerJoinTop10(): Collection<MemberTotalData>
}