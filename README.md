<p align="center">
  <img src="https://github.com/user-attachments/assets/d6aa77ee-f49f-43e0-a47f-55731a9f31df">
</p>

#### 프로젝트 정보

아이티윌, '클라우드 가상화 기반 자바(JAVA) 개발자 양성과정'

개발기간: 2024.06 - 2024.09

#### 개발팀 소개

프론트엔드: 이지현
백엔드: 배은진, 권순만

#### 프로젝트 소개

**골프로**는 온라인 웹을 통해 골프 정보와 레슨을 제공하는 웹 페이지입니다.

프로젝트 기획 의도
1. 간편 시스템: 시간 및 공간적인 제한을 최소화
2. 비용 절감: 오프라인 레슨에 대비되는 무료 온라인 강의
3. 확장성: 골프 외 다른 카테고리로의 무수한 확장성

주요 기술 포인트
+ 영상 및 이미지 업로드
+ 동영상을 활용한 메인 배너 구축
+ 권한이 부여된 관리자 페이지 구현
+ 환전 시스템 구축

기대 효과
1. 수익 창출: 사용자 증가로 인한 수익 구조 확장
2. 레슨비 절감: 오프라인 레슨과 상반된 레슨비로 비용 절감
3. 맞춤성 학습: 개개인에게 맞춤화된 피드백 제공

시작 가이드

기술 스택

<img src="https://img.shields.io/badge/eclipse ide-2C2255?style=for-the-badge&logo=eclipse ide&logoColor=white">
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
<img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
<img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
<img src="https://img.shields.io/badge/oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/node.js-339933?style=for-the-badge&logo=Node.js&logoColor=white">
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
<img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
<img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

주요 기능
+ 동영상이나 사진을 포함한 게시글 업로드 가능
+ 일반 회원과 프로 회원에 따른 기능 구분
+ 마이페이지에서 회원 정보와 본인 활동 확인 가능
+ PC와 모바일에 따른 반응형 페이지 구현

회고
+ 오류 없이 프로그램이 실행되는 것뿐만 아니라, 서버 과부하 및 쿼리 단순화와 같은 성능 최적화 요소도 중요하다고 판단
+ 서버 부하 감소 및 최적화를 위해 Redis를 캐시 서버로 활용하고자 하였으나, Amazon ElastiCache를 구현하는 데 그치고 실제 활용 단계로 나아가지 못함
+ 결과적으로 서버가 감당할 수 없는 용량으로 인해 다운되는 상황이 발생하여 배포에 실패함
