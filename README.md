🛠️ PostgreSQL 계정 보안 및 환경 변수 로딩 문제 해결

📅 **해결 날짜**: 2025-02-01  

📌 문제 발견
PostgreSQL 계정 정보를 .env 파일에 저장하고, domov 라이브러리를 사용해 환경 변수를 로드하는 과정에서 Spring Boot가 실행되지 않는 버그가 발생했다.

🔍 문제 원인
Spring Boot 실행 시 .env 파일 내의 환경 변수가 제대로 로드되지 않아 애플리케이션이 멈추는 현상이 발생했다

java.lang.RuntimeException: Driver org.postgresql.Driver claims to not accept jdbcUrl, ${DB_URL}
➡️ Spring Boot가 DB_URL을 제대로 읽지 못하고, ${DB_URL} 그대로 문자열로 인식함.
➡️ 즉, 환경 변수(DB_URL)가 올바르게 로드되지 않았음.

🚀 해결 방법
터미널에서 아래 명령어를 실행하여 환경 변수를 수동으로 로드한 후 Spring Boot를 실행했다.

export $(grep -v '^#' .env | xargs) && ./gradlew bootRun
➡️ 위 명령어 실행 후, Spring Boot가 정상적으로 실행됨을 확인했다.

또한, Spring Boot 실행 후 중지한 뒤 다시 실행할 때는 추가적인 환경 변수 설정 없이도 정상적으로 작동했다.

./gradlew bootRun

🔎 해결 결과
최초 실행 시 환경 변수를 명시적으로 로드해주면 정상적으로 실행됨을 확인 ✅
한 번 실행한 이후에는 추가 설정 없이도 ./gradlew bootRun 만으로 정상 작동 ✅
PostgreSQL 계정 정보가 안전하게 .env 파일에서 관리되도록 설정 완료 🔐
