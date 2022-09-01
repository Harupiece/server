# 💎Harupiece💎 - 백엔드 파트

건강 챌린지 플랫폼, 하루조각


## 하루조각 소개  
* __"코로나 때문에 늘 무기력한 나, 체력과 건강을 생각해서 뭔가는 해야 할 것 같은데.. 의지박약? 작심삼일?__"   
* __" 어떻게 하면 스트레스는 덜 받으면서, 건강한 습관을 만들 수 있을까?"__  
* 혹시 여러분의 이야기는 아닌가요? 이를 위해 기획한 것이 바로 바로 !! 
* __건강챌린지 플랫폼 하루조각입니다!!__

### ✨ 챌린지를 통해 원하는 목표에 한 발자국 더 다가갈 수 있습니다
> 자신이 원하는 목표에 맞는 챌린지를 신청하고, 서로를 응원하다보면 어느새 목표 달성!
### 📅 기간을 설정하고 원하는 챌린지를 만들수 있습니다
> 본인이 원하는 챌린지가 없거나 기간이 안 맞는다면 챌린지를 만들어 진행할 수 있습니다
> 기간을 짧게 진행하여 성공하는 습관도 기를수 있습니다 
### 💖 응원하기를 통해 다른 챌린저들에게 힘을 줄 수 있습니다
> 다른 유저의 응원을 확인하며 나도 할수 있다 라는 자신감을 얻고 서로 응원하여 목표를 달성할수 있습니다
### 📧 채팅을 통해 소통 할 수 있습니다
> 채팅을 통해 목표 달성에 관한 꿀팁이나 잔잔한 소통으로 친해질 수 있습니다
### 💎 조각을 모아 성취감을 달성하세요 
> 다른 유저를 응원하거나 인증샷을 올리면 조각을 받을 수 있습니다   
> 일정 조각을 모으면 구슬로 변경되며, 구슬을 모으는 재미에 챌린지를 더 열심히 하게 됩니다

### ⏰개발기간
- 2021년 07월 23일 ~ 2021년 08월 31일

### 👨‍👨‍👧‍👧  팀원
- 💻 Backend
    - 김선용, 김진태, 박연우, 최왕규
- 💻 Frontend 
    - 김태현, 정민주, 편원준
- 💻 Dedigner 
    - 안지혜, 유수빈
   
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
- QueryDSL 적용 (간단한 쿼리는 바닐라 JPA 이용)
- 스케줄러를 이용한 챌린지 일정 변경
- 스케줄러를 이용한 주기적인 공식 챌린지 자동 생성 및 일정 변경

#### Posting + Certification
- Posting GET / POST / PUT / DELETE
- Certification POST 
- QueryDSL 적용
- 인증여부에 따른 포인트 지급.
- 상태관리를 위한 Scheduler (벌크 쿼리)


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
