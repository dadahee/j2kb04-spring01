# 3주차 추가 학습내용

- [3주차 추가 학습내용](#3주차-추가-학습내용)
  - [1. 스프링 JdbcTemplate](#1-스프링-jdbctemplate)
    - [1.1. java.util.Optional<T> 클래스](#11-javautiloptionalt-클래스)
      - [1.1.1. 정의](#111-정의)
      - [1.1.2. 왜 사용할까?](#112-왜-사용할까)
      - [1.1.3. 특징](#113-특징)
      - [1.1.4. Optional 객체 생성](#114-optional-객체-생성)
      - [1.1.5. Optional 객체 접근](#115-optional-객체-접근)
      - [1.1.6. 그 외 메소드](#116-그-외-메소드)
    - [1.2. 템플릿 메소드 패턴](#12-템플릿-메소드-패턴)
    - [1.3. JdbcTemplate과 RowMapper](#13-jdbctemplate과-rowmapper)
      - [1.3.1. JdbcTemplate 사용법](#131-jdbctemplate-사용법)
      - [1.3.2. RowMapper](#132-rowmapper)
  - [2. JPA](#2-jpa)
    - [2.1. Generation Stragety](#21-generation-stragety)
    - [2.2. EntityManager](#22-entitymanager)
      - [2.2.1. Entity](#221-entity)
      - [2.2.2. EntityManager](#222-entitymanager)
      - [2.2.3. EntityManagerFactory](#223-entitymanagerfactory)
  - [3. 스프링 데이터 JPA](#3-스프링-데이터-jpa)
    - [3.1. Querydsl](#31-querydsl)

------

## 1. 스프링 JdbcTemplate

### 1.1. java.util.Optional<T> 클래스

#### 1.1.1. 정의

- `T` 타입의 객체를 포장해주는 래퍼 클래스(Wrapper Class)

#### 1.1.2. 왜 사용할까?

- `NullPointerException` 예외 처리 용이

#### 1.1.3. 특징

모든 타입의 참조 변수를 저장 가능

#### 1.1.4. Optional 객체 생성

- `Optional.of(object)`: object가 null이면 `NullPointerException` 발생
- `Optional.ofNullable(object)`: `NullPointerException` 발생 X

    ```java
    Optional<String> not_null_optional = Optional.of("Optional object");
    Optional<String> nullable_optional = Optional.ofNullable(null); // ok
    ```

#### 1.1.5. Optional 객체 접근

- `get()`
  - Optional 객체에 저장된 값 접근
  - 저장된 값이 null일 경우, NoSuchElementException 예외 발생
- `isPresent()`
  - null 여부 확인
  - get()을 사용할 때 먼저 사용하여 객체에 안전하게 접근하자!

```java
Optional<String> opt = Optional.ofNullable(null);
System.out.prinltn(opt); // Optional.empty
// System.out.println(opt.get()) // ERROR! - NoSuchElementException

Optional<String> opt2 = Optional.ofNullable("hello");
System.out.prinltn(opt2); // Optional[hello]
System.out.println(opt2.get()) // hello

if (opt.isPresent()){
    System.out.println(opt.get()); // 실행 x
}
```

#### 1.1.6. 그 외 메소드

- `orElse()`: 저장된 값이 존재하면 그 값을 반환, 값이 존재하지 않으면 인수로 전달된 값을 반환
- `orElseGet()`: 저장된 값이 존재하면 그 값을 반환하고, 값이 존재하지 않으면 인수로 전달된 람다 표현식의 결괏값을 반환
- `orElseThrow()`: 저장된 값이 존재하면 그 값을 반환하고, 값이 존재하지 않으면 인수로 전달된 예외를 발생시킴

    ```java
    Optional<String> optional = Optional.empty(); // Optional 객체를 null로 초기화
    System.out.println(optional.orElse("Hello Optional")); // Hello Optional 출력
    ```

- 참고자료: [TCP School](http://tcpschool.com/java/java_stream_optional)


### 1.2. 템플릿 메소드 패턴

- 어떤 작업을 처리하는 일부분을 하위 클래스로 캡슐화 -> 전체 수행 구조는 바꾸지 않으면서 특정 단계에서 수행하는 내역을 바꾸는 패턴
- 장점
  - 전체적으로 동일하면서 부분적으로 다른 구문으로 구성된 메소드의 코드 중복을 최소화할 때 유용
  - 동일한 기능을 상위 클래스에서 정의, 확장/변화가 필요한 부분만 하위 클래스에서 구현
  - 전체적인 알고리즘을 보호
- 역할이 수행하는 작업
  - AbstractClass
    - 템플릿 메서드를 정의하는 클래스
    - 하위 클래스에 공통 알고리즘을 정의하고 하위 클래스에서 구현될 기능을 primitive 메서드 또는 hook 메서드로 **정의**하는 클래스
  - ConcreteClass
    - 물려받은 primitive 메서드 또는 hook 메서드(아무 일도 하지 않거나 기본 행동을 정의하는 메소드)를 **구현**하는 클래스
    - 상위 클래스에 구현된 템플릿 메서드의 일반적인 알고리즘에서 하위 클래스에 적합하게 primitive 메서드나 hook 메서드를 오버라이드하는 클래스
- 참고자료: [템플릿 메소드 패턴](https://gmlwjd9405.github.io/2018/07/13/template-method-pattern.html)

### 1.3. JdbcTemplate과 RowMapper

- Jdbc의 흐름: DB 연동에 필요한 객체 생성 -> 쿼리 실행 -> 객체 닫기(close())
- -> JdbcTemplate을 이용하여 간략화

#### 1.3.1. JdbcTemplate 사용법

1. DB 연동: JdbcTemplate에 DataSource 설정
2. 쿼리 실행
   - 조회(SELECT): query() 메소드
     - 파라미터로 전달받은 sql 쿼리문을 시행하고 RowMapper를 이용하여 결과를 자바 객체로 변환
     - List<T> query(String sql, RowMapper<T> rowMapper)
     - List<T> query(String sql, Object[] args, RowMapper<T> rowMapper)
     - List<T> query(String sql, RowMapper<T> rowMapper, Object... args)
     - cf) queryForObject() 메소드: 쿼리 실행 결과가 한 행인 경우 사용
       - ex) `Integer memberCount = jdbcTemplate.queryForObject("select count(*) from member", Integer.class)`
   - 변경(INSERT/UPDATE/DELETE): update() 메소드
     - int update(String sql)
     - int update(String sql, Object... args)
 

#### 1.3.2. RowMapper

```java
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
```

- `mapRow()` 메소드: SQL 실행 결과로 구한 Resultset에서 한 행의 데이터를 읽어와 자바 객체로 변환

- query() + RowMapper 예제
  
    ```java
    private RowMapper<Member> memberRowMapper() {
        return new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return member;
            }
        };
    }
    ```
  
    ```java
    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);
        return result.stream().findAny();
    }
    ```

참고자료: [JdbcTemplate을 이용한 쿼리 실행](https://jaehoney.tistory.com/34)

------

## 2. JPA

### 2.1. Generation Stragety

- (데이터베이스 테이블의) 기본 키(Primary Key)
  - 자연 키(Natural Key): 의미 있는 키
    - ex)전화번호, 주민번호, 이메일
  - 대체 키(Surrogate Key): 임의로 생성된 키

- JPA(Java Persistence API)는 데이터베이스 테이블 대체 키를 기본 키로 자동 생성하는 기능 지원
  - Entity 클래스에 @Id 어노테이션과 결합하여 GenveratedKeyValue 어노테이션을 추가
  - 강의 내용 중에서
  
    ```java
    @Entity
    public class Member {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        // 생략
    }
    ```

</br>

- Generation Strategy
  - AUTO(default): JPA 구현체가 자동으로 생성 전략 결정
  - IDENTITY: 기본 키 생성을 데이터베이스에 위임
    - @GeneratedValue(strategy = GenerationType.IDENTITY)
  - SEQUENCE: 데이터베이스 시퀀스를 사용하여 기본 키를 할당
    - 데이터베이스 시퀀스: 유일한 값을 순서대로 생성하는 데이터베이스 오브젝트
    - 방법
      - @SequenceGenerator 생성
        - name: 식별자 생성기 이름 지정
        - sequenceName: DB에 등록되어 있는 시퀀스 이름 지정
        - initialValue: 초깃값
        - allocationSize: 호출당 증가하는 값
      - @GeneratedValue에서 strategy 지정 및 generator를 해당 이름으로 지정
  - TABLE: DB에 키 생성 전용 테이블을 하나 만들고 이를 이용하여 기본 키 생성
    - 방법
      - @TableGenerator 생성
        - name: 식별자 생성기 이름
        - table: 테이블명
        - pkColumnValue: 
        - allocationSize: 호출당 증가하는 값
      - @GeneratedValue에서 strategy 지정 및 generator를 해당 이름으로 지정

</br>

- IDENTITY -> SEQUENCES 전략

    ```java
    @Entity
    @SequenceGenerator(name = "MEMBER_ID_GENERATOR", sequenceName = "MEMBER_ID", initialValue = 1, allocationSize = 1)
    public class Member {
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_ID_GENERATOR")
        private long id;
    }
    ```

- IDENTITY -> TABLE 전략
  
    ```java
    @Entity
    @TableGenerator(name = "MEMBER_ID_GENERATOR", table = "MEMBER_SEQUENCES", pkColumnValue = "MEMBER_ID", allocationSize = 1)
    public class Member {
        @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_ID_GENERATOR")
        private Long id;

        // 생략
    }
    ```

</br>

참고자료: [키 생성 전략](https://www.popit.kr/%ED%95%98%EC%9D%B4%EB%B2%84%EB%84%A4%EC%9D%B4%ED%8A%B8%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%EC%9E%90%EB%8F%99-%ED%82%A4-%EC%83%9D%EC%84%B1-%EC%A0%84%EB%9E%B5%EC%9D%84-%EA%B2%B0%EC%A0%95%ED%95%98/)
참고자료: [JPA 기본 키 전략](https://feco.tistory.com/96)

### 2.2. EntityManager

#### 2.2.1. Entity

- DB에서 영속적으로 저장된 데이터를 자바 객체로 매핑하여 **인스턴스 형태로 존재하는 데이터**
- 생명 주기
  - 비영속(new/transient): 영속성 컨텍스트와 관련 없는 상태
    - 엔티티 객체를 생성했으나 저장하지 않은 상태 -> 영속성 컨텍스트에 저장 x
  - 영속(managed): 영속성 컨텍스트에 저장된 상태
    - 엔티티 매니저를 통해 영속성 컨텍스트에 저장 및 관리되는 상태
    - `em.persiste(member);`
  - 준영속(detached): 영속성 컨텍스트에 저장 되었다가 분리된 상태
    - 엔티티 매니저의 detach(), close(), clear()를 통해 영속 -> 준영속으로 변경된 상태
  - 삭제(removed): 삭제된 상태
    - 엔티티를 영속성 컨텍스트와 데이터베이스에서 삭제한 상태

#### 2.2.2. EntityManager

- 엔티티를 관리하는 역할을 수행하는 클래스
- 내부에 **영속성 컨텍스트(Persistence Context)**를 두어 엔티티를 관리
  - Persistence Context: 엔티티를 영구 저장하는 환경


#### 2.2.3. EntityManagerFactory

EntityManager 인스턴스를 관리

------

## 3. 스프링 데이터 JPA

### 3.1. Querydsl

Querydsl 정적 타입을 이용해서 SQL과 같은 쿼리를 생성할 수 있도록 해 주는 프레임워크다. 문자열로 작성하거나 XML 파일에 쿼리를 작성하는 대신, Querydsl이 제공하는 플루언트(Fluent) API를 이용해서 쿼리를 생성할 수 있다.

- Fluent API를 사용할 때의 장점
  - IDE의 코드 자동 완성 기능 사용
  - 문법적으로 잘못된 쿼리를 허용하지 않음
  - 도메인 타입과 프로퍼티를 안전하게 참조할 수 있음
  - 도메인 타입의 리팩토링을 더 잘 할 수 있음

등등  

양이 너무 많아서 링크만 걸어두겠습니다 T.T 생김새만 봐두면 좋을 것 같아요.

[Querydsl](https://querydsl.com/static/querydsl/4.0.1/reference/ko-KR/html_single/)
[QueryDSL 적용하기](https://velog.io/@tigger/QueryDSL)