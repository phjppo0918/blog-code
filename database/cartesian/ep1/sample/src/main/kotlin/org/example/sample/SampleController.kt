package org.example.sample

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController(
    private val sampleService: SampleService
) {

    @GetMapping("/cartesian-left")
    fun cartesianLeft() : Collection<MemberResponse> = sampleService.getCartesianLeft()

    @GetMapping("/cartesian-inner")
    fun cartesianInner() : Collection<MemberResponse> = sampleService.getCartesianInner()

    @GetMapping("/separate-query")
    fun separateQuery(): Collection<MemberResponse> = sampleService.getSeparateQuery()

    @GetMapping("/cartesian-left/top10")
    fun cartesianLeftTop10() : Collection<MemberResponse> = sampleService.getCartesianLeftTop10()

    @GetMapping("/cartesian-inner/top10")
    fun cartesianInnerTop10() : Collection<MemberResponse> = sampleService.getCartesianInnerTop10()

    @GetMapping("/separate-query/top10")
    fun separateQueryTop10(): Collection<MemberResponse> = sampleService.getSeparateQueryTop10()


}