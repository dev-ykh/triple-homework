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
   
   [response]
      - data       /* response data */
        - userId   /* 유저 ID */
        - totalPoints /* 총 포인트 점수 */
        - pointList   /* 포인트 List */
          - point     /* 포인트 점수 */
          - type      /* 포인트 적립 및 회수 타입 */
          - createdAt /* 생성일시 */
      - code          /* response code */
      - description   /* response 설명 */
   [response 예시]
      {
        "data": {
            "userId": "4e71407b-55c9-4c76-b050-261fb55ac85e",
            "totalPoints": 3,
            "pointList": [
                {
                    "point": 1,
                    "type": "ADD_PHOTOS",
                    "createdAt": "2021-10-04 06:19:23"
                },
                {
                    "point": 1,
                    "type": "ADD_FIRST_REVIEW",
                    "createdAt": "2021-10-04 06:19:23"
                },
                {
                    "point": 1,
                    "type": "ADD_CONTENTS",
                    "createdAt": "2021-10-04 06:19:23"
                }
            ]
        },
        "code": 0,
        "description": "정상 처리 되었습니다."
      }
   ```

- 포인트 적립
   ```
   [method] POST
   [url] /v1/points/{userId}
   [request body]
      - type     /* 근거타입 (REVIEW 등)*/ 
      - action   /* 엑션타입 (ADD, MOD, DELETE) */
      - placeId  /* 여행장소 ID */
      - content  /* 리뷰 내용 */
      - attachedPhotoIds  /* 리뷰 사진 List */
      - reviewId /* 리뷰 ID */
   [request 예시]
      {
         "type": "REVIEW",
         "action": "ADD",
         "placeId": "b63eb02f-34a3-4832-a938-37576ce82ac4",
         "content": "멋진 곳입니다!!",
         "attachedPhotoIds": ["fc1acb5c-e98e-4acb-a49c-a3571121e701"],
         "reviewId": "4e71407b-55c9-4c76-b050-261fb55ac85e"
      }
  
   [response]
      - data       /* response data */
        - userId   /* 유저 ID */
        - totalPoints /* 총 포인트 점수 */
        - pointList   /* 포인트 List */
          - point     /* 포인트 점수 */
          - type      /* 포인트 적립 및 회수 타입 */
          - createdAt /* 생성일시 */
      - code          /* response code */
      - description   /* response 설명 */
   [response 예시]
      {
        "data": {
            "userId": "4e71407b-55c9-4c76-b050-261fb55ac85e",
            "totalPoints": 3,
            "pointList": [
                {
                    "point": 1,
                    "type": "ADD_PHOTOS",
                    "createdAt": "2021-10-04 06:19:23"
                },
                {
                    "point": 1,
                    "type": "ADD_FIRST_REVIEW",
                    "createdAt": "2021-10-04 06:19:23"
                },
                {
                    "point": 1,
                    "type": "ADD_CONTENTS",
                    "createdAt": "2021-10-04 06:19:23"
                }
            ]
        },
        "code": 0,
        "description": "정상 처리 되었습니다."
      }
   ```

#### 기타

- 인증 : JWT을 이용한 인증이 필요하지만 만료시간이 있는 JWT의 특성상 토큰 발생 API도 필요하기 때문에 생략함.
- DB 접속정보 : DB 접속정보는 yml에 기입하는 것이 아니라 환경변수, AWS Secret, vault등으로 취득하는 것이 보안에 좋음. 과제라는 특성에 맞춰 yml 기입 방식으로 진행함. 
