# Title: [NWeek] 김경환

---

## 미션 요구사항 분석 & 체크리스트

- 미디엄은 블로그 플랫폼이다.
- 본인의 글을 유료화 할 수 있다.
- 미디엄에 가입 이후에 유료 멤버십에 가입하면 유료글을 볼 수 있다.
- 멤버십의 유지비용은 달에 2천원이다.
- 미디엄은 한달에 한번 유료글 작성자에게 조회수를 기준으로 멤버십 수익의 일정부분을 캐시로 정산해준다.
- 해당 캐시는 사이트 내에서 돈처럼 사용가능하고 원할 때 환전할 수 잇다.
- 이번 전반기 미션에서는 멤버십기능 정산기능을 제외한 나머지 기능을 구현한다.

### 필수미션

- 회원기능
>- [x] 가입
>- [x] 로그인
>- [x] 로그아웃
- 글 CRUD
>- [x] 홈
>- [x] 글 목록 조회
>- [x] 내 글 목록 조회
>- [x] 글 상세내용
>- [x] 글 작성
>- [x] 글 수정
>- [x] 글 삭제
>- [x] 특정 회원의 글 모아보기


---

1주차 미션 요약

---

**[접근방법]**

- 회원가입 기능에 toastr을 사용했습니다.
- BindingResult 객체를 사용하여 validation을 통해 유효성 검증을 수행하고 유효하지 않을 시 .getFieldError.getDefaultMessage 메소드를 통해 문자열 값을 얻어 toastr로 송출하도록 했습니다.
  - BindingResult를 적용시키기 위해 Article에도 RsData를 적용했습니다.
- js가 꺼져 있어도 검증이 작동하고 켜져 있을 경우엔 쿼리가 날아가는 것을 최대한 줄이기 위해 아이디 중복 검사 로직을 제외한 모든 로직들을 js와 자바소스코드에 각각 작성했습니다.
- navbar 와 pagination, articleTable은 추후의 유지보수를 위해 객체화했다.
- MemberJoinForm과 ArticleWriteForm을 클래스로 두어 가독성을 높이고 유지보수에 편의를 주었다.
- 특정 회원의 글을 모아보기 항목은 Repository에 containing을 이용한 메소드를 만들어 사용하였다.

---
**[특이사항]**
- navbar 객체화 중 경로 설정에서 차질을 빗었지만 강사님 도움으로 해결했다.
```html
<!-- 기존 코드 -->
<nav th:replace="~{global/navbar :: navbarFragment}"></nav>
<!-- 수정된 코드 -->
<nav th:replace="~{navbar :: navbarFragment}"></nav>
```
- TDD 환경을 만들어보려고 했으나 오류가 생겼다. 에러 메시지를 따라가보니 AppConfig 클래스의 다음 코드에서 문제가 발생하고 있었다
```java
@Value("${custom.site.baseUrl}")
public void setSiteBaseUrl(String siteBaseUrl) {
AppConfig.siteBaseUrl = siteBaseUrl;
}
```
- test.yml에 포트 설정이 안되어 있어 컴파일에러가 발생한 것이었다.
  - test.yml에도 포트 설정을 함으로서 문제를 해결했다.
- 아이디 중복 확인 로직을 위한 MemberControllerTest의 t3메소드를 구현하는 중 스크립트 내의 메시지 내용을 비교해보려고 했으나 스크립트 내의 메시지는 유니코드로 변환되어 있어 실패했다.
- 모든 회원에게 ROLE_USER 권한을 부여하고 admin에게만 ROLE_ADMIN 권한을 추가로 부여했다. 
- login 할때도 BindingResult 를 활용하여 경고 메시지를 toastr을 통해 송출해보려했으나 PostMapping 메서드 없이 진행되었기 때문에 html 내 경고문으로 대체했다.
- Bootstrap을 기반으로 만들어진 paging 코드를 Daisyui에 적용하려고 했지만 형식이 달라 헤매었다
  - button 을 사용할 경우 안의 실제 인터페이스에선 button이 아닌 button 안의 텍스트를 눌렀을 때만 작동하였다.
    - 공식에 나와있는 button 이 아닌 a 을 사용함으로써 해결
- isPublished 필드값을 html의 체크박스를 통해 전달 받는 과정에서 오류가 있었다.
  - html에서 해당 인풋 엘리멘털의 name 값을 published로 수정함으로써 해결

---
**[수정사항]**
