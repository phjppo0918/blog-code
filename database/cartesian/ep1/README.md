# 쿼리 분리 VS 카테시안 곱 허용하기
![img_5.png](img_5.png)
![img_6.png](img_6.png)

# 들어가기 앞서
이 포스팅은 DOKBARO를 개발하면서 경험한 것을 기반으로 제작하였습니다.
### DOKBARO란 ?


# 서론
- 간혹, DB에 접근 하는 수를 줄이기 위해 카테시안 곱을 묵시하는 경우가 있음
- 근데, 그러면 불필요한 메모리 사용량도 야기됨
- 그러면 카테시안 곱을 취하는게 좋을지, 피한 이후 쿼리를 쪼개는게 좋을 지
- 

# 사례 설명
성능 테스트를 위한 간단한 사례를 설명드리겠습니다.  
![img_3.png](img_3.png)

위 상황에서, 우리는 member가 속한 팀 목록과, member의 역할을 조회하고자 합니다.
샘플 데이터는 member 10만 건, role 30만 건, team 30만 건을 기반으로 진행하였습니다.

테스트에서 사용된 코드는 다음 링크를 참고해주시면 감사하겠습니다

# 카테시안 곱이란?
본격적으로 들어가기 앞서, 카테시안 곱에 대해 간단히 설명 드리고자 합니다.  
한 테이블을 대상으로 2개 이상의 table에 조인 시 결합으로 이루어질 수 있는 모든 경우의 수를 결과값으로 변환하는 현상을 말합니다.  
데카르트 곱이라고도 합니다.

예를 들어, "반장"과 "떡잎마을지킴이"를 하고있는 "철수"가 "흰둥이FC" 와 "햇님반" 에 속해있으면, 단일 쿼리 조회 시 다음과 같이 결과가 도출됩니다.
![img_4.png](img_4.png)
이러한 현상을 카테시안 곱이라 합니다.


# 성능 측정
## 단순 쿼리 시간 측정
우선, 가볍게 쿼리 실행 시간이 얼마나 걸리는지 아래와 같이 실행 시간을 측정했습니다!
```kotlin
fun getCartesianLeft(): Collection<MemberResponse> {
        val startTime = System.nanoTime()
        val selectTotal: Collection<MemberTotalData> = sampleRepository.selectTotalLeftJoin()
        val endTime = System.nanoTime()

        println("cartesian-left db connection time = ${(endTime - startTime) / 1000000000.0}s")
        ...
    }

    fun getCartesianInner(): Collection<MemberResponse> {
        val startTime = System.nanoTime()
        val selectTotal: Collection<MemberTotalData> = sampleRepository.selectTotalInnerJoin()
        val endTime = System.nanoTime()

        println("cartesian-inner db connection time = ${(endTime - startTime) / 1000000000.0}s")
        ...
    }

    fun getSeparateQuery(): Collection<MemberResponse> {
        val startTime = System.nanoTime()
        val members = sampleRepository.selectMember()
        val roles = sampleRepository.selectRole().groupBy { it.memberId }
        val teams = sampleRepository.selectTeam().groupBy { it.memberId }
        val endTime = System.nanoTime()

        println("separate query db connection time = ${(endTime - startTime) / 1000000000.0}s")
        ...
    }
```
그랬더니 결과는...다음과 같습니다!

```
cartesian-left db connection time = 7.908462167s // 3rd
cartesian-inner db connection time = 6.413592375s // 2nd
separate query db connection time = 2.898180333s  // 1st
```

쿼리를 쪼개서 실행한 것이, 카테시안 곱을 허용한 것 보다 실행 시간이 더 짧은 것을 알 수 있었습니다.   
더 자세한 성능 지표를 보기 위해 ngrinder를 통한 테스트도 진행해볼까요?

## ngrinder 을 이용한 테스트 결과
vuser를 더 늘리고 싶었는데... 여기서 더 늘리면 실패 확률이 많이 생겨서,  
모두가 success 할 수 있는 vuser 개수를 기준으로 진행하였습니다.

### cartesian - left join 
![img.png](img.png)
### cartesian - inner join
![img_1.png](img_1.png)
### 쿼리 분리
![img_2.png](img_2.png)

ngrinder를 통한 성능 지표를 봐도 쿼리 분리하는 케이스가 월등히 높은 것을 알 수 있었습니다!  
카테시안 곱을 허용하는 것에 비해 약 3배 정도 성능이 좋은 것을 알 수 있었습니다.

# 마무리
이처럼, 카테시안 곱이 성능 저하를 야기할 수도 있다는 것을 이번 실험을 통해 알아볼 수 있었습니다.  
앞으로는 가능한, 쿼리를 분리하여 사용하는 것이 좋을 것 같네요!  
실제로 DOKBARO 프로젝트에서도 카테시안 곱으로 동작한 부분을 쿼리 분리로 해결해서  
비약적으로 성능 향상을 얻은 경우도 있었습니다!  
물론, 애플리케이션 상태에 따라 다를 수 있다 생각합니다.  
위 테스트는 로컬 내에서만 테스트를 진행하였기 때문에, 다른 환경에서는 결과가 달라질 수도 있을 것 같네요!  
각자 환경에 맞게 진행하면 좋을 것 같습니다.  

그럼 이것으로 이번 포스팅을 마치겠습니다. 긴 글 읽어주셔서 감사합니다!