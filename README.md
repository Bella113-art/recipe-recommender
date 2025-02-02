# Recipe Recommender 프로젝트 개요

## 🚀 1. 프로젝트 소개
- 사용자가 냉장고에 남은 재료를 입력하면, 해당 재료로 만들 수 있는 레시피를 추천해주는 앱
- 재료 입력 후 레시피 추천, 검색 히스토리 저장 및 불러오기, 레시피 즐겨찾기, Spoonacular 활용

## 🛠️ 2. 기술 스택
- **프론트엔드:** Kotlin (Android)
- **백엔드:** Java (Spring Boot)
- **데이터베이스:** PostgreSQL
- **API:** Spoonacular Recipe API
- **배포:** AWS (추후 예정)

## 🔧 3. 설치 및 실행 방법
### 💻 3.1 Git에서 프로젝트 다운 받기
git clone https://github.com/Bella113-art/recipe-recommender.git # 프로젝트 클론
cd recipe-recommender # 프로젝트 폴더로 이동

### 💻 3.2 백엔드 실행 방법
cd backend  # 백엔드 폴더로 이동 
./gradlew bootRun  # 백엔드 실행
export $(grep -v '^#' .env | xargs)  # 환경 변수 로드 (Mac/Linux) -- 위 방법으로 실행 안될 시

**📌 Windows 사용자의 경우**
- export 대신 수동으로 .env 파일의 변수를 추가해야 할 수도 있음

### 💻 3.3 안드로이드 앱 실행 방법
1. Android Studio 실행
2. File → Open 선택 후 프로젝트 (recipe-recommender) 열기
3. 실행할 에뮬레이터 또는 실제 기기 설정
4. 상단의 ▶️ Run 버튼 클릭

**📌 안드로이드 앱 실행 전, 백엔드 서버가 실행되고 있는지 확인 필요!**

## 📡 4. API 명세
- 레시피 검색: /recipes/search
- 즐겨찾기 추가: /recipes/favorite
- 최근 검색 기록 불러오기: /recipes/history

## 🏗️ 5. 데이터베이스 설계
- PostgreSQL 사용, 유저별 즐겨찾기 저장
- 주요 테이블: users(예정), favorites_recipe, search_history(예정)

## 🔒 6. 보안 및 Git 관리
- 환경 변수는 .env 파일에서 관리
- backend 폴더 내에서 실행 (cd backend && ./gradlew bootRun)
- Git 브랜치 관리:
  main: 안정적인 코드 유지
  backend: 백엔드 개발 전용
  feature/backend-security: 백엔드 보안 기능 추가
  docs/update-readme: 문서 업데이트

## 🤝 7. 연습 중 (지침)
- PR 기반 Git 관리
- 커밋 메시지 컨벤션 준수
- 백엔드 및 문서 수정 시 feature/ 또는 docs/ 브랜치에서 작업 후 PR 요청

## 📄 8. 추가 문서
📂 API 문서 (추후 작성 예정)
📂 데이터베이스 설계 (추후 작성 예정)

**📌 이 문서는 계속 업데이트될 예정입니다.**

-------------------------------------------------------------------------------------------

# 백엔드 버그 해결 기록
## 1. 🛠️ PostgreSQL 계정 보안 및 환경 변수 로딩 문제 해결

### 📅 해결 날짜: 2025-02-01  

### 📌 문제 발견
PostgreSQL 계정 정보를 .env 파일에 저장하고, domov 라이브러리를 사용해 환경 변수를 로드하는 과정에서 Spring Boot가 실행되지 않는 버그가 발생했다.

### 🔍 문제 원인
Spring Boot 실행 시 .env 파일 내의 환경 변수가 제대로 로드되지 않아 애플리케이션이 멈추는 현상이 발생했다.

**java.lang.RuntimeException: Driver org.postgresql.Driver claims to not accept jdbcUrl, ${DB_URL}**

➡️ Spring Boot가 DB_URL을 제대로 읽지 못하고, ${DB_URL} 그대로 문자열로 인식함.
➡️ 즉, 환경 변수(DB_URL)가 올바르게 로드되지 않았음.

### 🚀 해결 방법
터미널에서 아래 명령어를 실행하여 환경 변수를 수동으로 로드한 후 Spring Boot를 실행했다.

**export $(grep -v '^#' .env | xargs) && ./gradlew bootRun**

➡️ 위 명령어 실행 후, Spring Boot가 정상적으로 실행됨을 확인했다.

또한, Spring Boot 실행 후 중지한 뒤 다시 실행할 때는 추가적인 환경 변수 설정 없이도 정상적으로 작동했다.

**./gradlew bootRun**

### 🔎 해결 결과
최초 실행 시 환경 변수를 명시적으로 로드해주면 정상적으로 실행됨을 확인 ✅
한 번 실행한 이후에는 추가 설정 없이도 ./gradlew bootRun 만으로 정상 작동 ✅
PostgreSQL 계정 정보가 안전하게 .env 파일에서 관리되도록 설정 완료 🔐

## 2. 🛠️ PostgreSQL 계정 보안 및 환경 변수 로딩 문제 해결 (2)

### 📅 해결 날짜: 2025-02-02  

### 📌 문제 발견
시간이 좀 지난 뒤에 ./gradlew bootRun 명령어로 백엔드를 실행했는데 다시 Spring Boot가 실행되지 않는 문제 발견

### 🔍 문제 원인
전에 해결 방법 'export $(grep ~' 명령어를 실행한 영향이 지속되지 않는 문제가 있는 것 같다.

### 🚀 해결 방법
**BackendApplication.java** 파일의 내용을 아래와 같이 추가로 수정한 후 Spring Boot를 실행했다.

import io.github.cdimascio.dotenv.Dotenv; **# dotenv 라이브러리 추가** ✅

@SpringBootApplication(scanBasePackages = "com.example.backend") 
public class BackendApplication {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load(); // .env 파일 자동 로드
		System.setProperty("DB_URL", dotenv.get("DB_URL")); **# 환경변수 추가** ✅
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME")); **# 환경변수 추가** ✅
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD")); **# 환경변수 추가** ✅
		SpringApplication.run(BackendApplication.class, args);
	}
}

### 🔎 해결 결과
BackendApplication.java 내부에 환경 변수를 추가해주니 추가 설정 없이 ./gradlew bootRun 만으로 정상 작동하는 것을 확인했다 ✅

