# 나는 왜 jooq에 빠지게 됐을까?

# 들어가기 앞서
이 포스팅은 DOKBARO를 개발하면서 경험한 것을 기반으로 제작하였습니다.
### DOKBARO란 ?
자기계발과 성장을 위해 독서와 스터디를 활용하는 **개발자들을 위한 퀴즈 학습 플랫폼, DOKBARO**입니다.

개발 서적을 즐겨 읽지만, **매번 내용을 제대로 이해했는지 확인하기 어렵지 않으셨나요?** 혹은 이해 부족으로 인해 **독서 스터디가 소수만 적극적으로 참여하는 형태로 변질되는 경험**을 하셨을지도 모릅니다.

그래서, **DOKBARO는**

📚 **퀴즈 출제 및 풀이 기능**으로 도서 내용을 재미있고 효과적으로 이해하도록 도와드려요.

💡 **스터디 리포트 기능**으로 스터디원들이 책에 대해 자유롭게 의견을 나누고, 서로의 학습 현황을 확인할 수 있어요.

**DOKBARO와 함께라면** 도서 이해도를 높이고, 스터디 활동을 보다 풍성하고 활발하게 만들어 이상적인 독서 환경을 경험하실 수 있습니다. ✌️

현재는 알파테스트 중에 있으니, 조금만 더 보완해서 여러분들께 선사하도록 하겠습니다!



# 왜 jooq를 선택하게 됐을까?
DOKBARO를 개발하면서 DB 쿼리 인터페이스로 JooQ를 선택했는데요!  
다른 후보군들과 비교분석을 하면서 JooQ가 가장 무난하다는게 제 결론이었습니다.  
mybatis / jpa / queryDSL 등 다양한 방식이 있었는데 제가 왜 JooQ를 선택했을까요??

### 1. mybatis에 대한 치명적인 단점
#### 동적쿼리 하...더 쉽지 않네
다음은 동적쾨리 예시입니다 
```xml
<select id="getStudentInfo" parameterType="hashMap" resultType="hashMap">
    SELECT *
    FROM BOARD
    WHERE USE_YN = 'Y'
    <choose>
        <when test='"writer".equals(searchType)'>
            AND WRITER = #{searchValue}
        </when>
        <when test='"content".equals(searchType)'>
            AND CONTENT = #{searchValue}
        </when>
        <otherwise>
            AND TITLE = #{searchValue}
        </otherwise>
    </choose>
</select>

<select id="getStudentInfoList" parameterType="hashMap" resultType="hashMap">
SELECT *
FROM STUDENT
WHERE STUDENT_ID IN
    <foreach collection="params" item="item" open="(" separator="," close=")">
        #{item.studentId}
    </foreach>
</select>

       
<select id="getStudentInfoList" parameterType="hashMap" resultType="hashMap">
    SELECT *
    FROM STUDENT
    WHERE STUDENT_ID IN
    <foreach collection="params" item="item" open="(" separator="," close=")">
        #{item}
    </foreach>
</select>

[출처] [MyBatis]MyBatis 동적 쿼리 문법 정리(if, choose, trim, where, set, foreach, selectKey)|작성자 로그
```
choose, when, foreach 등 mybatis에서 제공하는 문법을 통해 동적 쿼리를 작성해야합니다.  
타입이 따로 명시되어있지 않고, test 내부 넣는 부분을 String으로 입력해야하는 등 휴먼 에러를 발생하기 너무 좋은 환경입니다.  

#### object mapping 노가다
```XML
    <!-- 복잡한 결과 매핑 정의 -->
    <resultMap id="DetailedDepartmentMap" type="com.example.model.Department">
        <id property="id" column="dept_id"/>
        <result property="name" column="dept_name"/>
        
        <!-- 1:1 관계 매핑 -->
        <association property="location" javaType="com.example.model.Location">
            <id property="id" column="loc_id"/>
            <result property="address" column="loc_address"/>
            <result property="city" column="loc_city"/>
        </association>
        
        <!-- 1:1 복합 객체 매핑 -->
        <association property="stats" javaType="com.example.model.DepartmentStats">
            <result property="employeeCount" column="emp_count"/>
            <result property="avgSalary" column="avg_salary"/>
            <result property="totalBudget" column="total_budget"/>
        </association>
        
        <!-- 1:N 관계 매핑 -->
        <collection property="teams" ofType="com.example.model.Team">
            <id property="id" column="team_id"/>
            <result property="teamName" column="team_name"/>
            
            <!-- 중첩된 1:1 관계 -->
            <association property="teamLeader" javaType="com.example.model.Employee">
                <id property="id" column="leader_id"/>
                <result property="name" column="leader_name"/>
                
                <!-- 중첩된 1:1 상세 정보 -->
                <association property="detail" javaType="com.example.model.EmployeeDetail">
                    <result property="email" column="leader_email"/>
                    <result property="phone" column="leader_phone"/>
                </association>
            </association>
            
            <!-- 중첩된 1:N 관계 -->
            <collection property="members" ofType="com.example.model.Employee">
                <id property="id" column="emp_id"/>
                <result property="name" column="emp_name"/>
                
                <!-- 중첩된 컬렉션 매핑 -->
                <collection property="skills" ofType="com.example.model.Skill">
                    <id property="id" column="skill_id"/>
                    <result property="name" column="skill_name"/>
                    <result property="level" column="skill_level"/>
                </collection>
            </collection>
            
            <!-- Map 타입 매핑 -->
            <collection property="activeProjects" javaType="map" ofType="com.example.model.Project">
                <id property="id" column="project_id"/>
                <result property="name" column="project_name"/>
                <result property="status" column="project_status"/>
            </collection>
        </collection>
    </resultMap>
```
이런걸 매 객체마다 만들어줘야 된다? 진짜 개발하다 잠듭니다.

### 2. jpa의 한계점. 영속성 컨텍스트에 대한 회의감
일단 JPA가 JooQ랑 비교한다는 것 자체가 약간 애매하긴 합니다. JPA는 ORM 계열이고, JooQ는 그저 쿼리를 편하게 작성하려는 툴이니까요.  
이 부분은 제가 DOKBARO 프로젝트에서 왜 JPA를 적용하지 않았는지를 중점으로 봐주시면 감사하겠습니다.


#### 영속성 컨텍스트...왜 써?
영속성 컨텍스트의 가장 큰 장점은 변경감지(Dirty checking)라 생각합니다.  
허나 DOKBARO는 아예 외부 인프라 의존성을 분리하여 설계하였기에 변경감지에 대해 큰 효과를 누릴 수 없다 생각했습니다.  
그래서 과감하게 JPA를 사용하지 않는 판단을 했습니다.


#### 쿼리 로직을 JPA가 100% 담당할 수 있어?
도메인 관점에서 볼 때 요구사항 변경에 가장 치명적인 케이스 중 하나가 조회 로직 변경이라 생각합니다.  
주어진 엔티티 내 외적으로 추가적인 데이터를 보여주려고 하면, 그때부터 도메인이 꼬이기 십상입니다.  
그럼 매 쿼리로직을 join / fetch join으로 커버가 가능할까요? 뭐 한다고 마음 먹으면 뭘 못하겠습니까?  
entity 레벨로 조회하는 예시를 보여드리겠습니다.  
```java
@Entity
@Table(name = "orders")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "Order.withCustomer",
        attributeNodes = {
            @NamedAttributeNode("customer")
        }
    ),
    @NamedEntityGraph(
        name = "Order.withCustomerAndItems",
        attributeNodes = {
            @NamedAttributeNode("customer"),
            @NamedAttributeNode(value = "orderItems", subgraph = "items")
        },
        subgraphs = {
            @NamedSubgraph(
                name = "items",
                attributeNodes = {
                    @NamedAttributeNode("product")
                }
            )
        }
    ),
    @NamedEntityGraph(
        name = "Order.complete",
        attributeNodes = {
            @NamedAttributeNode("customer"),
            @NamedAttributeNode(value = "orderItems", subgraph = "items"),
            @NamedAttributeNode("delivery"),
            @NamedAttributeNode("payment")
        },
        subgraphs = {
            @NamedSubgraph(
                name = "items",
                attributeNodes = {
                    @NamedAttributeNode("product"),
                    @NamedAttributeNode("discount")
                }
            )
        }
    )
})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    
    private LocalDateTime orderDate;
    private OrderStatus status;
    
    // getter, setter 생략
}
```
쿼리 로직을 이렇게 엔티티 내로 물고 들어가면 결국 엔티티에 대한 복잡도도 증가하기 마련입니다.  
그래서 제 생각은 도메인 자체는 커멘드 로직 위주로 구성하고, 쿼리 로직은 아예 분리를 해야 한다 생각합니다.  
이에 대한 SQS, SQRS 관련 내용은 다른 포스팅에서 예를 보여드리겠습니다.  
즉, 쿼리는 SQL 쿼리 그대로를 보여줘야 한다 생각합니다.  
그래서 흔히 사용하는 방법이 JPQL, Criteria QueryDSL 등이 있겠네요.  
#### QueryDSL의 몰락
근래 국내에서 김영한님의 강의로 QueryDSL 관심도가 부쩍 늘은 것 같네요.  
허나, 그 전에 QueryDSL 이 3년 이상 방치가 되고, 최근에 OpenFeign이 이어 받아서 계속 개발중이긴 한데, 앞으로 어떻게 될지 좀더 두고 볼 필요가 있겠네요. 더 많은 내용은 아래 블로그에 정리가 되어 있습니다.  
https://yeoon.tistory.com/167

저도 기존에는 QueryDSL 자주 사용하였는데요! 다른사람들이 뭐라고 하든, 나만 편하게 쓰면 되지 이런 심정으로 쭉 쓰다가,  
작년에 제가 직접 QueryDSL 버그를 경험하고 그때부터 정이 뚝 떨어진 것 같네요.  
https://github.com/spring-projects/spring-boot/issues/38527  

물론 지금 JPA를 애초에 도입하려 하지 않아서 QueryDSL에 대한 고려도 크게 하지 않았습니다.  
따라서 DOKBARO에 사용할 라이브러리를 JooQ로 정했습니다.

# JooQ 장점
그럼 jooq를 써봤을 때 느꼈던 좋은 점을 쭉 설명 드릴게요!  
일단, 문법 자체는 너~무 쉽습니다. 그냥 SQL 쿼리 짜는 것 같아요!  
SQL 쿼리에 프로그래밍 언어로 동적쿼리 분기처리 등을 편하게 할 수 있었습니다.  
```Kotlin
fun findAllMySolveSummary(
		memberId: Long,
		pageOption: PageOption<MySolvingQuizSortKeyword>,
	): Collection<MySolveSummary> {
		val record: Result<out Record> =
			dslContext
				.select(
					SOLVING_QUIZ.ID,
					SOLVING_QUIZ.CREATED_AT,
					BOOK.IMAGE_URL,
					BOOK_QUIZ.ID,
					BOOK_QUIZ.TITLE,
				).from(SOLVING_QUIZ)
				.join(BOOK_QUIZ)
				.on(BOOK_QUIZ.ID.eq(SOLVING_QUIZ.QUIZ_ID).and(BOOK_QUIZ.DELETED.isFalse))
				.join(BOOK)
				.on(BOOK.ID.eq(BOOK_QUIZ.BOOK_ID))
				.where(SOLVING_QUIZ.MEMBER_ID.eq(memberId))
				.orderBy(toMySolvingQuizOrderQuery(pageOption), SOLVING_QUIZ.ID)
				.limit(pageOption.limit)
				.offset(pageOption.offset)
				.fetch()

		return solvingQuizMapper.toMySolveSummary(record)
	}

	private fun toMySolvingQuizOrderQuery(pageOption: PageOption<MySolvingQuizSortKeyword>): OrderField<out Any> {
		val query =
			when (pageOption.sort) {
				MySolvingQuizSortKeyword.TITLE -> BOOK_QUIZ.TITLE
				MySolvingQuizSortKeyword.CREATED_AT -> SOLVING_QUIZ.CREATED_AT
			}

		if (pageOption.direction == SortDirection.DESC) {
			return query.desc()
		}

		return query
	}
```
또한, 타입이 이미 정해지고, API가 다 규격화 되어서 메서드 체이닝으로 쭉쭉 작성해나갈 수 있던게 너무 좋았던 것 같아요!  
컴파일 레벨에서 쿼리 에러를 잡고, 자동완성을 통해서 개발해 나갈 수 있는게 큰 장점이라 생각합니다.

# jooQ 의 단점
### 1. 시작이 반이라는 말이 있다. jooq는 시작이 90%다.
초기 세팅이 너무너무 귀찮습니다. 확실한건, JDBC, mybatis, jpa, querydsl 보다도 세팅이 까다롭고,  
참고할 레퍼런스도 많지 않은 부분이 단점이라 생각합니다.

### 2. SQL 중심적임. DB 변경에 같이 움직인다. -> 외부 환경에 취약함
Jooq가 테이블 기반으로 Pojo나 DAO를 생성해주기 때문에 외부 환경 변화에 취약하다는 단점이 있습니다.  
그래서 DB 변경 사항에 대해 형상 관리가 충분히 되지 않으면 아주 그냥 좋아 죽습니다.  
만약 여러 서버가 한 DB를 바라보고 있다? 그럼 DB 변경 시 여러 서버를 같이 JooQ 쿼리 동기화를 해줘야하는 단점이 있습니다.  
개인적인 해결책으로 flyway 같은 DB 형상 관리 툴을 추가해주면 좋을 것 같다 생각하는데요!  
그래서 DOKBARO 서비스는 jooq - testcontainer - flyway 기반으로 설계했습니다. 

# 결론
이번 프로젝트를 진행하면서 Jooq를 처음 적용해봤는데요! 처음 사용함에도 불구하고, 손 쉽게 사용할 수 있었고,  
주변 사람들에게도 적극 추천하고 있어요! 꼭 한번 경험해보셨으면 좋을 것 같습니다.  
저는 jooq만 사용했지만, jpa-jooq 두 개를 연동해서 사용하는 것도 매우 좋을 것 같아요! 각자 환경에 고려해서 개발해나가면 좋을 것 같습니다!  
이상으로 포스팅 마치도록 하겠습니다. 감사합니다!