## 항해99 실전 프로젝트 (💎하루조각💎) - 백엔드 파트

<p align='center'>
<a href="https://harupiece.com/">  <img src="https://user-images.githubusercontent.com/79621675/131829950-29fbcb07-2c3b-4d95-a2e0-a492313bfce9.png" width="300px" /> </a>
</p>

###  클릭시 해당 서비스를 이용하실 수 있습니다. 👆👆
---

### 🔗 서비스 링크
https://harupiece.com/

### 💎하루조각💎 서비스 소개
하루조각은 건강 챌린지를 할수있도록 도와주는 서비스입니다.
- 스트레스를 줄이고 즐겁게 건강한 습관을 만들 수 있는 플랫폼

### ⏰개발기간
2021년 07월 23일 ~ 2021년 08월 31일

### 👨‍👨‍👧‍👧 백엔드 팀원
- 💻 Backend
    - 김선용, 김진태, 박연우, 최왕규
- [팀원 소개 페이지](https://little-viper-dae.notion.site/fe1d19c624bc4746b505b50e03b396b8 )
    
### 📕 테이블
- **테이블 설계** - <a href="https://github.com/OneDayPiece/server/wiki/ERDiaGram" >상세보기 이동</a>  
<!-- - **API 설계** - <a href="" >상세보기 이동</a> -->

### ⛏ 개발 환경
- Java 8
- Gradle
- Database - Mysql(AWS RDS)
- SpringBoot
- SpringDataJPA - QueryDSL
- Nginx ( LoadBalancing)
- Jenkins ( CI / CD )


### 🧱 Architecture ###
- <a href="https://github.com/OneDayPiece/server/wiki/Architecture" >상세보기 이동</a>  

### 💡 기능 💡
#### Security + 로그인 + Member 
- SSL
- CORS 
- Spring Security + JWT 기반 일반 로그인



#### Challenge
- Challenge GET / POST / PUT / DELETE
- paging applied 제목 검색, 소팅 검색(+전체 보기) 
- QueryDSL 적용
- 스케줄러를 이용한 챌린지 일정 변경
- 스케줄러를 이용한 주기적인 공식 챌린지 자동 생성 및 일정 변경

#### Posting 
- 인증샷 게시.
- 인증
- 포인트 지급
- 강퇴

#### 채팅 
- WebSocket
- Stomp
- Redis

### 이슈 관련

 - [채팅 서버 공유로 인한 메세지 중복 이슈](https://succulent-cadmium-bc4.notion.site/e5c0caab55834dd2a7e49361f045b558)
 - [설정파일을 Github에 안올리는 build하는방법](https://succulent-cadmium-bc4.notion.site/Github-build-29d71ec5870747228c42c962f6541862)
 - [채팅 메세지 욕설 필터링 기능 구현](https://succulent-cadmium-bc4.notion.site/fe2cf3d03cc240f1829fe69a3996f2e5)
 - [챌린지 강퇴 관련 이슈](https://succulent-cadmium-bc4.notion.site/08b12dfe5a004b708508a78bc0a8800e)
 - [챌린지관련 강퇴이슈 v2](https://succulent-cadmium-bc4.notion.site/v2-a1498ae972be47a7aaa041f560af2add)
 - [챌린지 관련 강퇴 이슈 v3](https://succulent-cadmium-bc4.notion.site/v3-2072fb3dc7394a8a859c6a2ec55e5dbf)
 - [Scheduler 중복 insert 이슈](https://succulent-cadmium-bc4.notion.site/Scheduler-insert-681167cf26724cfca12f249ea0893360)
 - [마이페이지 챌린지 히스토리 관련 이슈](https://succulent-cadmium-bc4.notion.site/bbbd8923fea64d62996f7bf06ee1556d)
 - [JPQL 사용시 무의미한 조인 발생 이슈](https://succulent-cadmium-bc4.notion.site/JPQL-303c4592a5364cc9bc28cc000bbd3b50)
