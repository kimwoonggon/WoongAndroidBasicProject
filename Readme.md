# 🍔 Macdonald Contents - 프로젝트 아키텍처 문서

## 1. 프로젝트 개요

| 항목 | 값 |
|------|-----|
| **프로젝트 이름** | macdonald_contents |
| **패키지 이름** | `com.woonggon.macdonald_contents` |
| **applicationId** | `com.woonggon.macdonald_contents` |
| **compileSdk** | 36 |
| **minSdk** | 24 (Android 7.0 Nougat) |
| **targetSdk** | 36 |
| **Java 호환성** | Java 11 |
| **Gradle** | 9.3.1 (Kotlin DSL) |
| **AGP** | 9.1.0 |
| **빌드 시스템** | Gradle + Version Catalog (libs.versions.toml) |

---

## 2. 전체 앱 흐름 (App Flow Diagram)

```
┌─────────────────────────────────────────────────────────────────┐
│                         앱 시작 (런처)                           │
│                      SplashActivity                             │
│                    (3초 대기 후 분기)                             │
└────────────────────────┬────────────────────────────────────────┘
                         │
            ┌────────────┴────────────┐
            │                         │
   auth.currentUser     auth.currentUser
      == null               != null
            │                         │
            ▼                         ▼
┌───────────────────┐     ┌───────────────────┐
│  LoginActivity    │     │   MainActivity    │
│  (이메일 로그인)   │     │  (메뉴 리스트)     │
└────────┬──────────┘     └──┬──────┬─────┬───┘
         │                   │      │     │
    ┌────┴────┐              │      │     │
    │         │              │      │     │
    ▼         ▼              ▼      ▼     ▼
┌────────┐ ┌──────────┐  ┌─────┐ ┌────┐ ┌──────────┐
│Join    │ │Main      │  │View │ │Book│ │Login     │
│Activity│ │Activity  │  │Act. │ │Mark│ │Activity  │
│(회원가입)│ │(로그인후) │  │(웹뷰)│ │Act.│ │(로그아웃) │
└────────┘ └──────────┘  └─────┘ └────┘ └──────────┘
```

### 상세 화면 전환 흐름

```
SplashActivity ──(로그인 X)──▶ LoginActivity ──(로그인 성공)──▶ MainActivity
      │                              │
      │                              └──(회원가입 버튼)──▶ JoinActivity
      │                                                       │
      │                              LoginActivity ◀──(회원가입 성공)──┘
      │                                    ▲                   │
      │                                    └──(로그인하러 가기)──┘
      │
      └──(로그인 O)──▶ MainActivity ──(아이템 클릭)──▶ ViewActivity
                            │                             │
                            ├──(북마크 버튼)──▶ BookMarkActivity
                            │
                            └──(로그아웃 버튼)──▶ LoginActivity
```

---

## 3. 프로젝트 디렉토리 구조

```
macdonald_contents/
├── build.gradle.kts                 # 루트 빌드 설정 (google-services 4.4.4)
├── settings.gradle.kts              # 프로젝트 설정 (rootProject.name)
├── gradle.properties
├── local.properties
├── gradlew / gradlew.bat
├── macdonald_architecture.md        # ← 이 문서
│
├── gradle/
│   ├── libs.versions.toml           # 버전 카탈로그
│   └── wrapper/
│       └── gradle-wrapper.properties
│
└── app/
    ├── build.gradle.kts             # 앱 모듈 빌드 설정
    ├── proguard-rules.pro
    │
    └── src/main/
        ├── AndroidManifest.xml      # 앱 매니페스트
        │
        ├── java/com/woonggon/macdonald_contents/
        │   ├── SplashActivity.kt    # 스플래시 (앱 진입점)
        │   ├── LoginActivity.kt     # 로그인
        │   ├── JoinActivity.kt      # 회원가입
        │   ├── MainActivity.kt      # 메인 메뉴 리스트
        │   ├── ViewActivity.kt      # 웹뷰 상세보기 + 북마크
        │   ├── BookMarkActivity.kt  # 북마크 리스트
        │   ├── RVAdapter.kt         # RecyclerView 어댑터
        │   ├── ContentsModel.kt     # 데이터 모델
        │   └── LoadingDialog.kt     # 로딩 다이얼로그 유틸리티
        │
        └── res/
            ├── layout/
            │   ├── activity_splash.xml        # 스플래시 UI
            │   ├── activity_login_page.xml    # 로그인 UI
            │   ├── activity_login.xml         # 회원가입 UI
            │   ├── activity_main.xml          # 메인 UI
            │   ├── activity_view.xml          # 웹뷰 UI
            │   ├── activity_book_mark.xml     # 북마크 UI
            │   ├── rv_item.xml                # RecyclerView 아이템
            │   └── dialog_loading.xml         # 로딩 다이얼로그
            │
            ├── drawable/
            │   ├── radius.xml                 # 둥근 모서리 shape
            │   ├── ic_launcher_background.xml
            │   └── ic_launcher_foreground.xml
            │
            ├── values/
            │   ├── colors.xml
            │   ├── strings.xml
            │   └── themes.xml
            │
            ├── values-night/                  # 다크모드 테마
            ├── mipmap-*/                      # 앱 아이콘 (hdpi~xxxhdpi)
            └── xml/                           # 백업/추출 규칙
```

---

## 4. 클래스 다이어그램

```
┌─────────────────────────────────────────────────────────────────────┐
│                        AppCompatActivity                            │
└──────────┬───────┬───────┬───────┬───────┬───────┬──────────────────┘
           │       │       │       │       │       │
           ▼       ▼       ▼       ▼       ▼       ▼
      ┌────────┐┌──────┐┌──────┐┌──────┐┌──────┐┌────────┐
      │Splash  ││Login ││Join  ││Main  ││View  ││Book    │
      │Activity││Act.  ││Act.  ││Act.  ││Act.  ││MarkAct.│
      └────────┘└──────┘└──────┘└──────┘└──────┘└────────┘
                                    │       │       │
                                    │       │       │
                                    ▼       ▼       ▼
                              ┌──────────────────────────┐
                              │  RVAdapter               │
                              │  (RecyclerView.Adapter)  │
                              │                          │
                              │  ┌─────────────────────┐ │
                              │  │ interface ItemClick  │ │
                              │  │  onCLick(View, Int)  │ │
                              │  └─────────────────────┘ │
                              │                          │
                              │  ┌─────────────────────┐ │
                              │  │ inner class ViewHolder││
                              │  │  bindItems(model)    │ │
                              │  └─────────────────────┘ │
                              └──────────────────────────┘
                                          │
                                          │ uses
                                          ▼
                              ┌──────────────────────────┐
                              │  ContentsModel           │
                              │  (data class)            │
                              │                          │
                              │  - url: String           │
                              │  - imageUrl: String      │
                              │  - titleText: String     │
                              └──────────────────────────┘

      ┌──────────────────────────────┐
      │  LoadingDialog (object)      │
      │  (싱글톤 유틸리티)             │
      │                              │
      │  - dialog: AlertDialog?      │
      │  + show(context, message)    │
      │  + dismiss()                 │
      └──────────────────────────────┘
         ↑ 사용하는 곳:
         LoginActivity, JoinActivity,
         ViewActivity, BookMarkActivity
```

---

## 5. Activity별 상세 설명

### 5.1 SplashActivity
| 항목 | 내용 |
|------|------|
| **레이아웃** | `activity_splash.xml` (ConstraintLayout) |
| **역할** | 앱 진입점 (LAUNCHER), 3초 스플래시 후 인증 상태에 따라 분기 |
| **Firebase** | `Firebase.auth.currentUser?.uid` 로 로그인 확인 |
| **분기 로직** | `uid == null` → LoginActivity / `uid != null` → MainActivity |
| **UI 요소** | "맥도날드\n먹고싶다" 텍스트 (40sp, #ff7f00, bold, 센터) |

### 5.2 LoginActivity
| 항목 | 내용 |
|------|------|
| **레이아웃** | `activity_login_page.xml` (LinearLayout, vertical, center) |
| **역할** | 이메일/비밀번호 로그인 |
| **Firebase** | `auth.signInWithEmailAndPassword(email, password)` |
| **UI 요소** | loginEmailArea (EditText), loginPasswordArea (EditText), loginBtn (Button), goToJoinBtn (Button) |
| **로딩** | LoadingDialog "로그인중입니다..." |
| **성공 시** | Toast "로그인 성공" → MainActivity → finish() |
| **실패 시** | Toast "로그인 실패: {에러메시지}" |

### 5.3 JoinActivity
| 항목 | 내용 |
|------|------|
| **레이아웃** | `activity_login.xml` (LinearLayout, vertical) |
| **역할** | 이메일/비밀번호 회원가입 |
| **Firebase** | `auth.createUserWithEmailAndPassword(email, password)` |
| **UI 요소** | emailArea (EditText), passwordArea (EditText), joinBtn (Button), goToLoginBtn (Button) |
| **로딩** | LoadingDialog "회원가입중입니다..." |
| **성공 시** | Toast "회원가입 성공" → LoginActivity → finish() |
| **실패 시** | Toast "회원가입 실패: {에러메시지}" |

### 5.4 MainActivity
| 항목 | 내용 |
|------|------|
| **레이아웃** | `activity_main.xml` (ConstraintLayout, 배경: #ff7f00) |
| **역할** | 맥도날드 메뉴 리스트 표시, 북마크/로그아웃 기능 |
| **RecyclerView** | GridLayoutManager(2열), RVAdapter 사용 |
| **데이터** | 10개의 하드코딩된 ContentsModel 아이템 |
| **UI 요소** | bookmarkBtn (TextView), logoutBtn (TextView), rv (RecyclerView) |
| **아이템 클릭** | ViewActivity로 이동 (url, title, imageUrl 전달) |
| **로그아웃** | `Firebase.auth.signOut()` → LoginActivity → finish() |

#### 메뉴 아이템 목록 (10개)
| # | titleText |
|---|-----------|
| 1 | 베토디바질크림치즈세트 |
| 2 | 맥스파이시바질크림치즈세트 |
| 3 | 빅맥세트 |
| 4 | 더블스파이시상하이버거세트 |
| 5 | 맥스파이시상하이버거세트 |
| 6 | 1955버거세트 |
| 7 | 더블 쿼터파운더 치즈 세트 |
| 8 | 쿼터파운더 치즈세트 |
| 9 | 맥크리스피 치킨 디럭스 세트 |
| 10 | 맥크리스피 치킨클래식 세트 |

### 5.5 ViewActivity
| 항목 | 내용 |
|------|------|
| **레이아웃** | `activity_view.xml` (ConstraintLayout) |
| **역할** | 웹뷰로 메뉴 상세 페이지 표시 + 북마크 저장/해제 토글 |
| **WebView** | WebViewClient, JS 활성화, DOM Storage 활성화 |
| **Intent 수신** | `url`, `title`, `imageUrl` |
| **UI 요소** | saveBtn (TextView: "저장"/"해제"), webView (WebView) |
| **북마크 확인** | 진입 시 Firebase RTDB에서 기존 북마크 여부 확인 |
| **북마크 저장** | `bookmarks/{uid}/{pushKey}` 에 ContentsModel 저장 |
| **북마크 해제** | `bookmarks/{uid}/{savedKey}` 삭제 |
| **로딩** | "북마크에 저장중입니다..." / "북마크 해제중입니다..." |

### 5.6 BookMarkActivity
| 항목 | 내용 |
|------|------|
| **레이아웃** | `activity_book_mark.xml` (ConstraintLayout) |
| **역할** | Firebase RTDB에서 북마크 리스트 로드 & 표시 |
| **RecyclerView** | GridLayoutManager(2열), RVAdapter 사용 |
| **Firebase** | `bookmarks/{uid}` 에서 addValueEventListener (실시간 업데이트) |
| **UI 요소** | "북마크" 타이틀 (TextView), bookmarkRv (RecyclerView) |
| **로딩** | LoadingDialog "북마크를 불러오는중입니다..." |

---

## 6. 데이터 모델

### ContentsModel

```kotlin
data class ContentsModel(
    val url: String = "",        // 메뉴 웹페이지 URL
    val imageUrl: String = "",   // 메뉴 이미지 URL
    val titleText: String = ""   // 메뉴 이름
)
```

> **Firebase 직렬화**: 모든 필드에 기본값 `""` 지정 (Firebase가 기본 생성자 필요)
> **필드 네이밍**: Kotlin `val` → Firebase에서 동일 이름으로 직렬화 (`url`, `imageUrl`, `titleText`)

---

## 7. Firebase 구조

### 7.1 Firebase Authentication

```
Authentication
├── Provider: Email/Password
└── Users
    ├── uid_001 (email: user1@example.com)
    ├── uid_002 (email: user2@example.com)
    └── ...
```

- **로그인**: `signInWithEmailAndPassword(email, password)`
- **회원가입**: `createUserWithEmailAndPassword(email, password)`
- **로그아웃**: `Firebase.auth.signOut()`
- **인증 확인**: `Firebase.auth.currentUser?.uid`

### 7.2 Firebase Realtime Database

```
{
  "bookmarks": {
    "<uid>": {
      "<pushKey_1>": {
        "url": "https://www.mcdonalds.co.kr/kor/menu/burger",
        "imageUrl": "https://www.mcdonalds.co.kr/upload/...",
        "titleText": "빅맥세트"
      },
      "<pushKey_2>": {
        "url": "...",
        "imageUrl": "...",
        "titleText": "..."
      }
    }
  }
}
```

- **저장**: `bookmarksRef.push().key` → `bookmarksRef.child(key).setValue(ContentsModel(...))`
- **삭제**: `bookmarksRef.child(savedKey).removeValue()`
- **읽기**: `addValueEventListener` (실시간) / `addListenerForSingleValueEvent` (1회)

### 7.3 Firebase 사용 흐름

```
┌──────────────┐         ┌──────────────────────┐
│ SplashActivity│ ──────▶ │ Firebase Auth        │
│              │  check   │ currentUser?.uid     │
└──────────────┘         └──────────────────────┘

┌──────────────┐         ┌──────────────────────┐
│ LoginActivity│ ──────▶ │ Firebase Auth        │
│              │  signIn  │ signInWithEmail...   │
└──────────────┘         └──────────────────────┘

┌──────────────┐         ┌──────────────────────┐
│ JoinActivity │ ──────▶ │ Firebase Auth        │
│              │  create  │ createUserWithEmail..│
└──────────────┘         └──────────────────────┘

┌──────────────┐         ┌──────────────────────┐
│ MainActivity │ ──────▶ │ Firebase Auth        │
│              │  signOut │ signOut()            │
└──────────────┘         └──────────────────────┘

┌──────────────┐         ┌──────────────────────┐
│ ViewActivity │ ──────▶ │ Firebase RTDB        │
│              │  R/W     │ bookmarks/{uid}/...  │
└──────────────┘         └──────────────────────┘

┌──────────────┐         ┌──────────────────────┐
│BookMarkAct.  │ ──────▶ │ Firebase RTDB        │
│              │  Read    │ bookmarks/{uid}      │
└──────────────┘         └──────────────────────┘
```

---

## 8. RecyclerView 아키텍처

```
┌─────────────────────────────────────────────────┐
│ MainActivity / BookMarkActivity                  │
│                                                  │
│  items: MutableList<ContentsModel>               │
│         │                                        │
│         ▼                                        │
│  ┌──────────────────────────────────────┐        │
│  │ RVAdapter                            │        │
│  │                                      │        │
│  │  context: Context                    │        │
│  │  List: MutableList<ContentsModel>    │        │
│  │  itemClick: ItemClick?               │        │
│  │                                      │        │
│  │  onCreateViewHolder()                │        │
│  │    └─ inflate(rv_item.xml)           │        │
│  │                                      │        │
│  │  onBindViewHolder()                  │        │
│  │    ├─ bindItems(model)               │        │
│  │    │    ├─ rv_text.text = titleText   │        │
│  │    │    └─ Glide.load(imageUrl)      │        │
│  │    └─ setOnClickListener → itemClick │        │
│  │                                      │        │
│  │  getItemCount() → List.size          │        │
│  └──────────────────────────────────────┘        │
│         │                                        │
│         ▼                                        │
│  ┌──────────────────────────────────────┐        │
│  │ RecyclerView                         │        │
│  │  layoutManager: GridLayoutManager(2) │        │
│  └──────────────────────────────────────┘        │
└─────────────────────────────────────────────────┘
```

### rv_item.xml 아이템 구조

```
┌──────────────────────────────┐
│ LinearLayout (vertical)       │
│ background: @drawable/radius  │
│ margin: 5dp                   │
│                               │
│  ┌──────────────────────────┐ │
│  │ CardView                 │ │
│  │  cornerRadius: 10dp      │ │
│  │                          │ │
│  │  ┌────────────────────┐  │ │
│  │  │ ImageView          │  │ │
│  │  │ (rvImageArea)      │  │ │
│  │  │ 120dp height       │  │ │
│  │  │ Glide로 이미지 로드  │  │ │
│  │  └────────────────────┘  │ │
│  └──────────────────────────┘ │
│                               │
│  ┌──────────────────────────┐ │
│  │ TextView (rvTextArea)    │ │
│  │ 25sp, bold, center       │ │
│  └──────────────────────────┘ │
└──────────────────────────────┘
```

---

## 9. UI 레이아웃 구조

### activity_splash.xml
```
ConstraintLayout
└── TextView ("맥도날드\n먹고싶다", 40sp, #ff7f00, bold, center)
```

### activity_login_page.xml (LoginActivity)
```
LinearLayout (vertical, gravity: center)
├── EditText #loginEmailArea (email)
├── EditText #loginPasswordArea (password)
├── Button #loginBtn ("로그인")
└── Button #goToJoinBtn ("회원가입하기", gray)
```

### activity_login.xml (JoinActivity)
```
LinearLayout (vertical)
├── EditText #emailArea (email)
├── EditText #passwordArea (password)
├── Button #joinBtn ("회원가입")
└── Button #goToLoginBtn ("로그인하러 가기", gray)
```

### activity_main.xml
```
ConstraintLayout (background: #ff7f00)
├── TextView #bookmarkBtn ("북마크", radius background)
├── TextView #logoutBtn ("로그아웃")
└── RecyclerView #rv (marginTop: 40dp, 전체 높이)
```

### activity_view.xml
```
ConstraintLayout
├── TextView #saveBtn ("저장"/"해제", 20sp)
└── WebView #webView (marginTop: 50dp, 전체)
```

### activity_book_mark.xml
```
ConstraintLayout
├── TextView ("북마크", 20sp, bold, center)
└── RecyclerView #bookmarkRv (marginTop: 50dp, 전체)
```

### dialog_loading.xml
```
LinearLayout (horizontal, padding: 24dp)
├── ProgressBar (40x40dp)
└── TextView #loadingMessage (16sp, marginStart: 16dp)
```

---

## 10. 외부 라이브러리 의존성

| 라이브러리 | 버전 | 용도 |
|-----------|------|------|
| **Glide** | 5.0.5 | 네트워크 이미지 로딩 (RecyclerView 아이템) |
| **Firebase BoM** | 34.11.0 | Firebase 버전 통합 관리 |
| **firebase-analytics** | (BoM 관리) | Firebase 기본 분석 |
| **firebase-auth** | (BoM 관리) | 이메일/비밀번호 인증 |
| **firebase-database** | (BoM 관리) | Realtime Database (북마크) |
| **Google Services Plugin** | 4.4.4 | Firebase 연동용 Gradle 플러그인 |

### AndroidX 라이브러리

| 라이브러리 | 버전 | 용도 |
|-----------|------|------|
| core-ktx | 1.18.0 | Kotlin 확장 함수 |
| appcompat | 1.7.1 | 하위 호환성 |
| material | 1.13.0 | Material Design 컴포넌트 |
| activity | 1.13.0 | Activity 관련 |
| constraintlayout | 2.2.1 | ConstraintLayout |

---

## 11. AndroidManifest 구성

```xml
<manifest>
    <uses-permission android:name="android.permission.INTERNET" />

    <application theme="@style/Theme.Macdonald_contents">

        <!-- 앱 진입점 (LAUNCHER) -->
        <activity name=".SplashActivity" exported="true">
            <intent-filter>
                <action name="android.intent.action.MAIN" />
                <category name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- 일반 Activity들 -->
        <activity name=".MainActivity" />
        <activity name=".LoginActivity"    exported="false" />
        <activity name=".JoinActivity"     exported="false" />
        <activity name=".ViewActivity"     exported="false" />
        <activity name=".BookMarkActivity" exported="false" />
    </application>
</manifest>
```

---

## 12. 유틸리티 컴포넌트

### LoadingDialog (Singleton Object)

```
┌─────────────────────────────────┐
│  object LoadingDialog           │
│                                 │
│  dialog: AlertDialog? = null    │
│                                 │
│  show(context, message)         │
│    ├─ dismiss() 기존 다이얼로그   │
│    ├─ inflate dialog_loading    │
│    ├─ set loadingMessage text   │
│    ├─ setCancelable(false)      │
│    └─ dialog.show()             │
│                                 │
│  dismiss()                      │
│    ├─ dialog?.dismiss()         │
│    └─ dialog = null             │
└─────────────────────────────────┘
```

**사용처:**
| Activity | 메시지 |
|----------|--------|
| LoginActivity | "로그인중입니다..." |
| JoinActivity | "회원가입중입니다..." |
| ViewActivity | "북마크에 저장중입니다..." / "북마크 해제중입니다..." |
| BookMarkActivity | "북마크를 불러오는중입니다..." |

---

## 13. Drawable 리소스

### radius.xml
```
shape (rectangle)
├── padding: 10dp (all sides)
├── solid color: #ffffff (흰색)
├── corners: 8dp (all corners)
└── stroke: 0dp, #808080
```
- **사용처**: rv_item.xml 배경, bookmarkBtn 배경

---

## 14. 데이터 흐름 다이어그램

```
┌────────────────────────────────────────────────────────────────┐
│                          데이터 흐름                            │
│                                                                │
│  [하드코딩 데이터]                                               │
│       │                                                        │
│       ▼                                                        │
│  MainActivity                                                  │
│  items: List<ContentsModel>                                    │
│       │                                                        │
│       ├──(RVAdapter)──▶ RecyclerView (2열 그리드)               │
│       │                      │                                 │
│       │                (아이템 클릭)                              │
│       │                      │                                 │
│       │                      ▼                                 │
│       │               Intent extras                            │
│       │               (url, title, imageUrl)                   │
│       │                      │                                 │
│       │                      ▼                                 │
│       │               ViewActivity                             │
│       │               ├── WebView.loadUrl(url)                 │
│       │               └── 저장 버튼 클릭                        │
│       │                      │                                 │
│       │                      ▼                                 │
│       │            ┌─── Firebase RTDB ───┐                     │
│       │            │  bookmarks/{uid}/   │                     │
│       │            │    /{pushKey}:      │                     │
│       │            │      ContentsModel  │                     │
│       │            └─────────────────────┘                     │
│       │                      │                                 │
│       │                      ▼                                 │
│       │              BookMarkActivity                          │
│       │              ├── addValueEventListener                 │
│       │              └── RecyclerView (2열 그리드)              │
│       │                                                        │
│  [Firebase Auth]                                               │
│       │                                                        │
│       ├── SplashActivity: currentUser 확인                     │
│       ├── LoginActivity: signIn                                │
│       ├── JoinActivity: createUser                             │
│       └── MainActivity: signOut                                │
└────────────────────────────────────────────────────────────────┘
```

---

## 15. 빌드 설정 요약

### build.gradle.kts (Root)
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
}
```

### build.gradle.kts (App)
```kotlin
plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.woonggon.macdonald_contents"
    compileSdk = 36
    minSdk = 24, targetSdk = 36
    Java 11
}

dependencies {
    // AndroidX (version catalog)
    // Glide 5.0.5
    // Firebase BoM 34.11.0
    //   - firebase-analytics
    //   - firebase-auth
    //   - firebase-database
}
```

### libs.versions.toml
```toml
agp = "9.1.0"
coreKtx = "1.18.0"
appcompat = "1.7.1"
material = "1.13.0"
activity = "1.13.0"
constraintlayout = "2.2.1"
```

---

## 16. 권한 (Permissions)

| 권한 | 용도 |
|------|------|
| `INTERNET` | Glide 이미지 로딩, WebView 페이지 로드, Firebase 통신 |

---

*이 문서는 macdonald_contents 프로젝트의 전체 아키텍처를 기술합니다.*
*마지막 업데이트: 2026-04-07*
