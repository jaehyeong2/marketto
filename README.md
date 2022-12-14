## marketto
> 간단한 마켓 프로젝트 api입니다!


## 프로젝트 학습 목적

- s3를 이용하여 파일을 업로드 해본다. 
- 계층 무한 대댓글 구조를 재귀 방식과 쿼리방식 두가지로 구현해본다.
- spring rest docs를 이용해본다.
- slack 과 연동하여 에러 알림을 보낸다.



## 기술 스택
- Java 11
- Spring boot & Spring securiy
- Spring Data Jpa & QueryDSL
- Gradle
- Swagger + Rest docs(추가 예정)
- H2, mariaDB

<!--
## 고민 포인트
> 프로젝트 개발 과정에서 있었던 고민들이며 이에 대한 저의 생각들은 고민별로 링크로 연결되어 있습니다.

- 도메인 설계(객체와 테이블)
    - [설계 주의 사항](https://velog.io/@kyle/%EB%A7%88%EC%BC%93-%EB%8F%84%EB%A9%94%EC%9D%B8-%EC%84%A4%EA%B3%84-1.-%EC%9C%A0%EC%9D%98%EC%A0%90#%EC%A7%80%EC%96%91%ED%95%98%EB%8A%94-%EC%97%B0%EA%B4%80-%EA%B4%80%EA%B3%84)
    - UML 및 설계
- [개발 프로세스](https://velog.io/@kyle/%EC%84%9C%EB%B2%84-API-%EB%AC%B8%EC%84%9C%ED%99%94Spring-Rest-docs)
- [문서화(Swagger | Spring Rest Docs)](https://velog.io/@kyle/Acceptance-Test-%EA%B0%9C%EB%B0%9C-%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4)
- [인증 및 인가](https://velog.io/@kyle?tag=%EC%9D%B8%EC%A6%9D%EA%B3%BC-%EC%9D%B8%EA%B0%80) 
    - [카카오 로그인](https://velog.io/@kyle/%EC%B9%B4%EC%B9%B4%EC%98%A4-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%99%B8%EB%B6%80-API-%EA%B4%80%EB%A6%AC)
    - [사용자의 역할에 따라 다른 권한 부여](https://velog.io/@kyle/%ED%9A%8C%EC%9B%90%EC%97%90-%EA%B6%8C%ED%95%9C%EC%9D%80-%EC%96%B4%EB%96%BB%EA%B2%8C-%EA%B4%80%EB%A6%AC%ED%95%A0%EA%B9%8C)
- [도메인 테스트 중복 제거](https://velog.io/@kyle/%EC%A0%9C%EB%84%A4%EB%A6%AD%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%9C-%EC%B6%94%EC%83%81%ED%99%94-%EB%B0%8F-%EC%A4%91%EB%B3%B5%EC%A0%9C%EA%B1%B0)
- 외부 API 관리 전략
    - [외부 API를 어떻게 관리할 것인가?](https://velog.io/@kyle/%EC%B9%B4%EC%B9%B4%EC%98%A4-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%99%B8%EB%B6%80-API-%EA%B4%80%EB%A6%AC)
    - [외부 API를 어떻게 테스트 할 것인가?](https://velog.io/@kyle/%EC%99%B8%EB%B6%80-API%EB%A5%BC-%EC%96%B4%EB%96%BB%EA%B2%8C-%ED%85%8C%EC%8A%A4%ED%8A%B8-%ED%95%A0-%EA%B2%83%EC%9D%B8%EA%B0%80)
    - 외부 API 데이터를 편리하게 Serialize & Deserialize 하는 방법        
- 이미지 업로드
- 조회성 쿼리 작업 
- 슬랙 알림 기능(외부 API와 함께 정리)
- 동적 쿼리문 작성 방법 
-->

## 기능 목록
> 세부적인 기능 목록을 나열한 것으로, 아직 완성되지 않은 기능도 포함되어 있습니다

##### 카테고리
- 관리자 권한체크
- 권한 기반 crud
##### 회원
- 회원가입 / 로그인
- Oauth2 구글 로그인
- 회원탈퇴시 soft delete 되었다가 3개월 후 db에서 실제 삭제
- 장바구니 & 찜한 상품
- 구매한 상품 내역
##### 게시물
- 게시물 전체 조회 + 날짜 검색 + 조건검색
- 회원 개인 게시물 작성 조회 + 날짜 검색
- 게시물 정렬 날짜 순 / 조회순
- 최다 좋아요 게시물 10건 조회 
- 게시물 저장 + s3 이미지 저장
- 게시물 좋아요 + 파이어베이스 푸시 알림
- 게시물 신고 + 파이어베이스 푸시 알림

##### 댓글
- 게시물 댓글 조회 (계층형)
- 회원 개인 작성 댓글 조회
- 댓글 작성 / 삭제 / 수정

##### 알림
- 게시물에 댓글 작성 / 게시물 신고 당할시 작성자에게 파이어베이스 푸시알림
- 프로젝트 에러 발생 시 슬랙 알림


<!--
##### 정렬
- 인기순
- 관련 상품
- 시간순(최신, 오래된)
- 판매완료 및 판매중
##### 알림
- 키워드 알림 : 관심 상품에 대한 키워드 알림
    - 서비스 내에서 뜨는 알림
- 전체 알림 : 슬랙에
    - 어떠한 상품이 등록되었습니다.
    - 어떤 공구관련 글이 등록되었습니다.
    - 판매가 완료되었습니다. (공구든 판매든)
- Q & A 관련 봇이나, 채팅
##### 관리자 페이지
- 정산이나 통계성 데이터
- 회원 권한 수정
- 상품 삭제


### 개발 진행과정

- [x] 도메인 모델 설계

- [x] Spring rest docs 의존성 추가

- [x] 로그인 (0605-06)
    - [x] Google : Oauth2 -> jwt -> jwt 기준 권한 관리
    - [x] Jwt Converter , Bearer Intercepter 구현
        - [x] Github, Google 공통 분모를 통해서 (id + organization) 을 통한 토큰 생성
        - [x] id + organization 을 통한 jwt 토큰 분해를 통한 Intercepter, MethodArgumentResolver 구현
    - [x] 유저 권한 부여 
        - [x] 관리자 : 전체 사이트에 대한 통계 정보를 확인할 수 있다. (관리자용 MethodArgumentResolver를 두면 될꺼같음.)
        - [x] 유저 : 대부분의 기능을 이용할 수 있음.
        - [x] 손님 : 가입 승인이 되기전의 사용자
        
- [x] LoginMember (토큰으로 로그인 한 사용자)
    - [x] read, update
    - [x] read, update 테스트 추가
    - [x] 권한 -> AllowRole 사용
    
- [x] 상품 (0607-0608)   
    - [x] 멀티 이미지 업로드
    - [x] 상품 crud
    - [x] 페이징
    
- [x] 댓글(0609-0610)
    - [x] 댓글 crud (PostId, Member를 기준으로)
    - [x] 사용자 Id를 기반으로 한 댓글 조회 기능
    - [x] Post 상세화면에서, 관련 댓글 함께 보여주기

- [x] 좋아요(0611-0613)
    - [x] 좋아요 crud (PostId, MemberId를 기준으로)
    - [x] 사용자 Id를 기반으로 좋아요 조회
    - [x] 인기 게시물 출력하기
   
- [x] 패키지 구조 변경

- [ ] 테스트 추가
    - [ ] Acceptance Tests
    - [ ] Layer & Domain Tests 
    - [x] S3 없이, 임의로 데이터 삽입 후 테스트
    - [x] S3 연동이후 임의로 추가한 데이터 변경
    - [ ] 테스트 코드 리팩토링

- [x] 검색 및 정렬
    - 정렬
        - [x] 카테고리를 기준으로 조회할 수 있다. -> category 
        - [x] 인기 상품을 조회할 수 있다. -> like
        - [x] 최근 작성일을 기준으로 조회할 수 있다. -> post 
        - [x] 특정 카테고리 및 최저 가격을 기준으로 조회할 수 있다. -> category & post 
 
    - 검색
        - [x] 글 제목의 키워드를 기준으로 검색할 수 있다.
        - [x] 작성자의 닉네임을 기준으로 검색할 수 있다.
         
- [x] 1 : 1 채팅 
    - [x] 객체 관계 설정
    - [x] 슬랙 연동

- [x] 알림
    - [ ] 메일 알림
    - [x] 슬랙 알림 

- [ ] 관리자
    - [ ] 통계성 데이터 화면
    - [ ] 권한 관리(Guest, User, Admin)
    
    -->
