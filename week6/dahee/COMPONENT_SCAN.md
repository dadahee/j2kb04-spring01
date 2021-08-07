# 컴포넌트 스캔

## 컴포넌트 스캔과 의존관계 자동 주입 시작하기

- 설정 클래스에 `@Bean`으로 일일이 등록하는 게 너무 귀찮다! -> 컴포넌트 스캔, 자동 의존관계 주입 사용

- 방법
  1. `@Configuration` 클래스에 `@ComponentScan` 추가
  2. 빈으로 등록할 클래스에 `@Component` 어노테이션 추가 (추상 x 구현에 o)
  3. 의존관계 주입 시 `@Autowired` 추가

- 동작 과정 및 원리
  - 컴포넌트 스캔
    - `@ComponentScan`은 `@Component`가 붙은 모든 클래스를 스프링 빈으로 등록
    - 이때, 스프링 빈 저장소에 `<빈 이름(맨 앞글자만 소문자로 변환): 빈 객체>` 추가
  - 의존 관계 자동 주입
    - `@Autowired`로 지정한 곳에서, 타입이 같은 빈을 찾아서 주입
    - 빈 이름을 명시할 경우 이름이 같은 빈을 찾아서 주입

## 탐색 위치와 기본 스캔 대상

### 기본 탐색 위치

- `@ComponentScan`이 붙은 설정 정보 클래스의 하위 패키지를 탐색
- 해당 어노테이션에 basePackages, basePackageClasses 옵션으로 지정할 수 있으나, 위치 지정 대신 설정 클래스 위치를 프로젝트 최상단에 두는 것을 권장함

### 컴포넌트 기본 스캔 대상

- `@Component` 전부
  - `@Component`를 포함하는 어노테이션: `@Controller`, `@Service`, `@Repository`, `@Configuration`, ...
  - 어노테이션 상속처럼 보이는 것은 스프링 내부적으로 지원하는 기능!
  - 따라서 얘네 모두 컴포넌트 스캔 대상이 됨

## 필터

- `@ComponentScan` 옵션으로 주어 스캔 대상 / 제외 대상 지ㅓㅈㅇ 가능
  - includeFilters: 굳이?
  - excludeFilters: 가끔 사용
    - 예시

        ```java
        @ComponentScan(
            excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
        )
        ```

- FilterType 옵션
  - ANNOTATION: 디폴트. 어노테이션을 인식하여 동작
  - ASSIGNABLE_TYPE: 지정한 타입과 자식 타입을 인식하여 동작
  - ASPECTJ: `AspectJ` 패턴 사용
  - REGEX: 정규 표현식
  - CUSTOM: `TypeFilter` 라는 인터페이스를 구현해서 처리

## 중복 등록과 충돌

- 자동 빈 등록 vs 자동 빈 등록
  - 자동 빈 등록된 빈 이름이 같을 경우 오류 발생

- 수동 빈 등록 vs 자동 빈 등록
  - 수동 등록이 우선권: 수동 빈이 자동 빈을 오버라이딩하여 수동이 등록됨
  - 오류 찾기 힘들어서 최근 스프링부트에서는 오류가 발생하도록 변경됨