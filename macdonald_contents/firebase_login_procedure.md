# Firebase 로그인 구현 절차 (macdonald_contents)

## 현재 구현된 앱 흐름
```
SplashActivity (3초 대기) → MainActivity (RecyclerView 목록) → ViewActivity (WebView 상세보기)
```

---

## Firebase 로그인 추가 절차

### Step 1: Firebase Console 설정
1. Firebase Console 접속
2. Authentication 활성화
3. 로그인 방법 선택 (이메일/비밀번호, Google 등)

### Step 2: 의존성 추가
`app/build.gradle.kts`에 추가:
```kotlin
implementation("com.google.firebase:firebase-auth")
```
(이미 BoM이 있으므로 버전 불필요)

### Step 3: 화면 개발
- `LoginActivity` 생성 — 이메일/비밀번호 입력 + 로그인/회원가입 버튼
- `JoinActivity` 수정 — 회원가입 UI

### Step 4: Firebase Auth 코드
- **회원가입**: `createUserWithEmailAndPassword()`
- **로그인**: `signInWithEmailAndPassword()`
- **로그인 상태 확인**: `FirebaseAuth.getInstance().currentUser`

---

## 변경 후 앱 흐름
```
SplashActivity (3초 + 로그인 상태 확인)
  ├── 로그인됨 → MainActivity → ViewActivity
  └── 미로그인 → LoginActivity
                    ├── 로그인 성공 → MainActivity
                    └── 회원가입 클릭 → JoinActivity → 가입 성공 → LoginActivity
```

---

## 로그인 상태 판단 원리

### currentUser로 판단
- `FirebaseAuth.getInstance().currentUser != null` → 로그인된 상태
- `FirebaseAuth.getInstance().currentUser == null` → 미로그인 상태
- Firebase Auth는 로그인 토큰을 **기기 로컬에 자동 저장**하므로, 앱을 껐다 켜도 로그인 유지

### SplashActivity 분기 예시
```kotlin
Handler().postDelayed({
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
        startActivity(Intent(this, MainActivity::class.java))
    } else {
        startActivity(Intent(this, LoginActivity::class.java))
    }
    finish()
}, 3000)
```

---

## signOut과 토큰 저장

### signOut 발동 시점
- `FirebaseAuth.getInstance().signOut()`을 **개발자가 직접 호출할 때만** 발동 (자동 발동 없음)
- 보통 앱 내 "로그아웃" 버튼을 만들어서 유저가 누르면 호출
- 앱 삭제 시에도 토큰이 같이 삭제되므로 결과적으로 로그아웃 상태

### 토큰 저장 위치
- Android **SharedPreferences**에 저장
- 경로: `/data/data/<패키지명>/shared_prefs/com.google.firebase.auth.api.Store.xml`
- 앱 샌드박스 안이라 **다른 앱이나 유저가 직접 접근 불가** (루팅 제외)
- 토큰은 자동 갱신되며, Firebase SDK가 만료 전에 알아서 리프레시

### 토큰 종류
| 토큰 | 용도 | 유효기간 |
|------|------|----------|
| ID Token | 사용자 인증 확인 | 1시간 (자동 갱신) |
| Refresh Token | ID Token 재발급용 | 반영구 (signOut 시 무효화) |
