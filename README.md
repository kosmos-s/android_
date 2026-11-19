# 📱 Android 과제 정리

> **202510987 김건보**

---

##  3주차 — 컴포즈 커피 (Compose Coffee)

<div align="center">
  <img src="https://github.com/user-attachments/assets/3e06d87d-7de2-4a0c-9bb8-4be5bfa56c87" alt="3주차 이미지" width="250" height="550"/>
</div>

* **Jetpack Compose**를 활용한 UI 구성
* 기본적인 **레이아웃 배치 및 스타일링** 연습
* ☕️ *“컴포즈 커피” 앱 완성*

---

##  4주차 — (작성 예정)

> 이 주차의 내용은 추후 추가될 예정입니다. ✏️

---

##  5주차 — 스톱워치 앱

<div align="center">
  <img src="https://github.com/user-attachments/assets/86ca6cc6-b228-4d22-bf4f-e9e3875c7335" alt="5주차 이미지" width="250" height="550"/>
</div>

* **스톱워치 기능 구현**
* **시간 측정, 시작/정지, 리셋 기능** 추가
* 🕒 *Compose 기반의 간단한 타이머 로직 구현*

---

##  6주차 — 버블버블 게임

<div align="center">
  <img src="https://github.com/user-attachments/assets/4d098d86-d5c4-4b91-92a1-c754244102b3" alt="6주차 이미지" width="250" height="550"/>
</div>

* 🎮 **버블버블 게임 제작**
* 터치 이벤트 처리 및 애니메이션 구현
* 점수 시스템 및 난이도 조정

---

### ✨ 요약

| 주차  | 주제     | 주요 내용            |
| --- | ------ | ---------------- |
| 3주차 | 컴포즈 커피 | UI 구성 및 컴포즈 연습   |
| 4주차 | -      | 추후 추가 예정         |
| 5주차 | 스톱워치   | 시간 측정 기능 구현      |
| 6주차 | 버블버블   | 게임 로직 및 애니메이션 구현 |

---

📌 **Repository 유지보수 중**

> 앞으로 추가될 주차별 실습도 차례로 업데이트될 예정입니다!

## 소스에서 실행하고 검증하기

Android Studio에서 `studyG` 폴더를 엽니다. 포함된 모듈은 `app`, `w3`, `w4`, `w5`, `w6`, `w7`입니다. SDK와 플러그인 버전은 각 모듈의 `build.gradle.kts` 및 `gradle/libs.versions.toml`을 따릅니다.

```powershell
cd studyG
.\gradlew.bat :app:testDebugUnitTest :w3:testDebugUnitTest :w4:testDebugUnitTest :w5:testDebugUnitTest :w6:testDebugUnitTest :w7:testDebugUnitTest
```

macOS/Linux에서는 `./gradlew`를 사용합니다. 이 명령은 로컬 단위 테스트만 실행합니다. 기본 `ExampleUnitTest`의 산술 검사 통과는 화면·기능 검증을 의미하지 않습니다. 기기·에뮬레이터에서는 각 모듈의 `connectedDebugAndroidTest`와 실제 사용 흐름을 별도로 확인하세요. SDK 경로가 담긴 `local.properties`와 빌드 결과는 커밋하지 않습니다. Windows에서는 한글·공백 없는 경로에서 빌드하는 것을 권장합니다.
