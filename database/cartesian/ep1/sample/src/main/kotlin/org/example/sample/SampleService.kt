package org.example.sample

import org.example.sample.data.MemberTotalData
import org.springframework.stereotype.Service

@Service
class SampleService(
    private val sampleRepository: SampleRepository,
) {
    fun getCartesianLeft(): Collection<MemberResponse> {
        val startTime = System.nanoTime()
        val selectTotal: Collection<MemberTotalData> = sampleRepository.selectTotalLeftJoin()
        val endTime = System.nanoTime()

        println("cartesian-left db connection time = ${(endTime - startTime) / 1000000000.0}s")

        return selectTotal.groupBy { Pair(it.id, it.name) }.map { (k, v) ->
            MemberResponse(
                id = k.first,
                name = k.second,
                roles = v.distinctBy { it.memberRoleId }.mapNotNull { it.memberRoleName },
                teams = v.distinctBy { it.memberTeamId }.mapNotNull { it.memberTeamName }
            )
        }
    }

    fun getCartesianInner(): Collection<MemberResponse> {
        val startTime = System.nanoTime()
        val selectTotal: Collection<MemberTotalData> = sampleRepository.selectTotalInnerJoin()
        val endTime = System.nanoTime()

        println("cartesian-inner db connection time = ${(endTime - startTime) / 1000000000.0}s")

        return selectTotal.groupBy { Pair(it.id, it.name) }.map { (k, v) ->
            MemberResponse(
                id = k.first,
                name = k.second,
                roles = v.distinctBy { it.memberRoleId }.mapNotNull { it.memberRoleName },
                teams = v.distinctBy { it.memberTeamId }.mapNotNull { it.memberTeamName }
            )
        }
    }

    fun getSeparateQuery(): Collection<MemberResponse> {
        val startTime = System.nanoTime()
        val members = sampleRepository.selectMember()
        val roles = sampleRepository.selectRole().groupBy { it.memberId }
        val teams = sampleRepository.selectTeam().groupBy { it.memberId }
        val endTime = System.nanoTime()

        println("separate query db connection time = ${(endTime - startTime) / 1000000000.0}s")

        return members.map { it ->
            MemberResponse(
                id = it.id,
                name = it.name,
                roles = roles[it.id]?.map { it.name } ?: emptyList(),
                teams = teams[it.id]?.map { it.name } ?: emptyList(),
            )
        }
    }

    fun getCartesianLeftTop10(): Collection<MemberResponse> {
        val startTime = System.nanoTime()
        val selectTotal: Collection<MemberTotalData> = sampleRepository.selectTotalLeftJoinTop10()
        val endTime = System.nanoTime()

        println("cartesian-left-top-10 db connection time = ${(endTime - startTime) / 1000000000.0}s")

        return selectTotal.groupBy { Pair(it.id, it.name) }.map { (k, v) ->
            MemberResponse(
                id = k.first,
                name = k.second,
                roles = v.distinctBy { it.memberRoleId }.mapNotNull { it.memberRoleName },
                teams = v.distinctBy { it.memberTeamId }.mapNotNull { it.memberTeamName }
            )
        }
    }

    fun getCartesianInnerTop10(): Collection<MemberResponse> {
        val startTime = System.nanoTime()
        val selectTotal: Collection<MemberTotalData> = sampleRepository.selectTotalInnerJoinTop10()
        val endTime = System.nanoTime()

        println("cartesian-inner-top-10 db connection time = ${(endTime - startTime) / 1000000000.0}s")

        return selectTotal.groupBy { Pair(it.id, it.name) }.map { (k, v) ->
            MemberResponse(
                id = k.first,
                name = k.second,
                roles = v.distinctBy { it.memberRoleId }.mapNotNull { it.memberRoleName },
                teams = v.distinctBy { it.memberTeamId }.mapNotNull { it.memberTeamName }
            )
        }
    }

    fun getSeparateQueryTop10(): Collection<MemberResponse> {
        val startTime = System.nanoTime()
        val members = sampleRepository.selectMemberTop10()
        val roles = sampleRepository.selectRoleTop10Member().groupBy { it.memberId }
        val teams = sampleRepository.selectTeamTop10Member().groupBy { it.memberId }
        val endTime = System.nanoTime()

        println("separate query-top-10 db connection time = ${(endTime - startTime) / 1000000000.0}s")

        return members.map { it ->
            MemberResponse(
                id = it.id,
                name = it.name,
                roles = roles[it.id]?.map { it.name } ?: emptyList(),
                teams = teams[it.id]?.map { it.name } ?: emptyList(),
            )
        }
    }
}