# triple_homework

### 개발 및 빌드환경

* JDK 11
* Kotlin 1.4.30
  * Kotlin Coroutine
* Spring Boot 2.4.5.RELEASE
  * Spring WebFlux
  * Spring DATA R2DBC
* MySql 5.7
  * jasync mysql 1.1.7
* gradle wrapper 7.2

### 빌드 및 실행 방법
- 8080 포트를 사용하므로 해당 포트가 사용중이면 안됨
- DB 접속 정보를 application.yml에 기입
- 실행 방법

   ```
   $ ./gradlew bootRun
   or
   gradlew.bat bootRun
   ```

### Application 내용
#### API 스팩

- 포인트 조회
   ```
   [method] GET
   [url] /v1/points/{userId}
   [request body]
      - type
      - action
      - placeId
      - content
      - attachedPhotoIds
      - reviewId
   [response]
      - data
        - userId
        - totalPoints
        - pointList
          - point
          - type
          - createdAt
      - code
      - description
   ```

- 포인트 적립
   ```
   [method] POST
   [url] /v1/points/{userId}
   [request body]
      - type
      - action
      - placeId
      - content
      - attachedPhotoIds
      - reviewId
   [response]
      - data
        - userId
        - totalPoints
        - pointList
          - point
          - type
          - createdAt
      - code
      - description
   ```

#### 기타

- 인증 : JWT을 이용한 인증이 필요하지만 만료시간이 있는 JWT의 특성상 토큰 발생 API도 필요하기 때문에 생략함.
- DB 접속정보 : DB 접속정보는 yml에 기입하는 것이 아니라 환경변수, AWS Secret, vault등으로 취득하는 것이 보안에 좋음. 과제라는 특성에 맞춰 yml 기입 방식으로 진행함. 
