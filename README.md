# **KeyValue gRPC Server**

이 프로젝트는 Kotlin과 gRPC를 사용하여 구현된 Key-Value 저장 및 조회 서버입니다. 멀티스레딩을 지원하는 gRPC 서버를 통해 문자열 형태의 Key와 Value를 저장, 삭제하고 조회할 수 있습니다.

 GitHub Projects 및 Issues를 사용해 작업을 관리하였으며,  feature 브랜치를 사용한 기능별 개발 및 Pull Request를 통한 main 브랜치로의 통합을 바탕으로 구현하였습니다. (https://github.com/kimihiqq/Kotlin-gRPC-Server/pulls?q=is%3Apr+is%3Aclosed )


## **기술 스택**

- Kotlin
- gRPC-Kotlin
- Docker
- MySQL (프로덕션), H2 (테스트)
- JPA


## **프로젝트 구조**

프로젝트는 다음과 같은 멀티 모듈 구조로 구성됩니다:

- **`presentation`**: gRPC 서비스 정의 및 구현
- **`application`**: 비즈니스 로직 조정 및 서비스 구현
- **`domain`**: 비즈니스 로직 및 엔티티 정의
- **`infrastructure`**:
    - **`boot`**: 어플리케이션 구동 및 환경 설정
    - **`provider`**: 데이터베이스 연결 및 기술적 구현


## **설치 및 실행 방법**

1. Docker 및 Docker Compose 설치
2. 프로젝트 클론:
    
    ```bash
    
    git clone git@github.com:kimihiqq/Kotlin-gRPC-Server.git
    ```
    
3. Docker 컨테이너 실행:
    
    ```bash
    cd Kotlin-gRPC-Server
    
    docker-compose up
    ```
    
4. 이후 새로운 터미널 창을 켜, 같은 위치에서 서버 실행:
    
    ```bash
    ./gradlew :infrastructure:boot:run
    ```
    

## 스키마 및 구현 이미지
<img width="547" alt="이미지1" src="https://github.com/kimihiqq/Kotlin-gRPC-Server/assets/134909318/fecd03a0-9e63-4f00-8a9f-0b02525c5339">
<img width="547" alt="이미지2" src="https://github.com/kimihiqq/Kotlin-gRPC-Server/assets/134909318/18bd68a5-7e7d-4e1e-9c0c-b52a7e814253">


