# diet_memo 프로젝트 아키텍처

## 1. 프로젝트 개요

운동/다이어트 메모를 기록하고 Firebase Realtime Database에 저장하는 Android 앱.  
Firebase 익명 인증(Anonymous Auth)으로 사용자를 구분하고, 사용자별로 메모를 관리한다.

---

## 2. 전체 디렉토리 구조

```
diet_memo/
├── build.gradle.kts                 # 루트 빌드 설정 (플러그인 선언)
├── settings.gradle.kts              # 프로젝트 설정
├── gradle.properties
├── local.properties
├── gradlew / gradlew.bat
│
├── gradle/
│   └── libs.versions.toml           # 버전 카탈로그 (AGP, Google Services, AndroidX)
│
└── app/
    ├── build.gradle.kts             # 앱 모듈 빌드 설정 (Firebase BoM, 의존성)
    ├── proguard-rules.pro
    │
    └── src/main/
        ├── AndroidManifest.xml      # 액티비티 등록, Launcher 설정
        │
        ├── java/com/woonggon/diet_memo/
        │   ├── SplashActivity.kt    # 스플래시 + Firebase 익명 로그인
        │   ├── MainActivity.kt      # 메인 화면 (메모 목록 + 작성)
        │   ├── ListViewAdapter.kt   # ListView용 커스텀 어댑터
        │   └── DataModel.kt         # 데이터 모델 (date, memo)
        │
        └── res/
            ├── layout/
            │   ├── activity_splash.xml   # 스플래시 화면 레이아웃
            │   ├── activity_main.xml     # 메인 화면 레이아웃
            │   ├── custom_dialog.xml     # 메모 작성 다이얼로그
            │   └── listview_item.xml     # 리스트뷰 아이템 레이아웃
            ├── drawable/
            │   ├── icon.png                  # 글쓰기 버튼 아이콘
            │   ├── ic_launcher_background.xml
            │   └── ic_launcher_foreground.xml
            ├── values/
            │   ├── colors.xml
            │   ├── strings.xml
            │   └── themes.xml
            ├── values-night/
            └── xml/
                ├── backup_rules.xml
                └── data_extraction_rules.xml
```

---

## 3. 앱 흐름 다이어그램

```
┌─────────────────────────────────────────────────────────────────┐
│                        앱 시작 (Launch)                          │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                               ▼
               ┌───────────────────────────────┐
               │       SplashActivity          │
               │  (activity_splash.xml)        │
               │                               │
               │  FirebaseAuth.getInstance()   │
               │                               │
               │  ┌─────────────────────────┐  │
               │  │ currentUser != null ?    │  │
               │  └─────┬───────────┬───────┘  │
               │     YES│           │NO        │
               │        ▼           ▼          │
               │   "원래 비회원    signIn      │
               │    로그인됨"    Anonymously()  │
               │        │       ┌───┴───┐      │
               │        │    성공│    실패│      │
               │        │       │   Toast│      │
               │        ▼       ▼       │      │
               │   3초 후 MainActivity   │      │
               │     로 이동 (Intent)    │      │
               └───────────────────────────────┘
                               │
                               ▼
┌──────────────────────────────────────────────────────────────────┐
│                        MainActivity                              │
│                    (activity_main.xml)                            │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐  │
│  │                     ListView (mainLV)                      │  │
│  │  ┌──────────────────────────────────────────────────────┐  │  │
│  │  │  listview_item.xml                                   │  │  │
│  │  │  ┌──────────────┐  ┌──────────────────────────────┐  │  │  │
│  │  │  │ 날짜 (30sp)  │  │ 메모 텍스트 (15sp)           │  │  │  │
│  │  │  └──────────────┘  └──────────────────────────────┘  │  │  │
│  │  └──────────────────────────────────────────────────────┘  │  │
│  │  ┌──────────────────────────────────────────────────────┐  │  │
│  │  │  (반복...)                                           │  │  │
│  │  └──────────────────────────────────────────────────────┘  │  │
│  └────────────────────────────────────────────────────────────┘  │
│                                                                  │
│                                          ┌──────────┐            │
│                                          │ 📝 (FAB) │            │
│                                          │ writeBtn │            │
│                                          └────┬─────┘            │
└───────────────────────────────────────────────┼──────────────────┘
                                                │ 클릭
                                                ▼
                        ┌───────────────────────────────────────┐
                        │     AlertDialog (custom_dialog.xml)   │
                        │     "운동 메모 다이얼로그"              │
                        │                                       │
                        │  ┌─────────────────────────────────┐  │
                        │  │ EditText (healthMemo)            │  │
                        │  │ "운동 메모를 입력해주세요"         │  │
                        │  └─────────────────────────────────┘  │
                        │  ─────────────────────────────────── │
                        │  ┌─────────────────────────────────┐  │
                        │  │ Button (dateSelectBtn)           │  │
                        │  │ "날짜를 선택해 주세요"            │──┼──▶ DatePickerDialog
                        │  └─────────────────────────────────┘  │
                        │  ┌─────────────────────────────────┐  │
                        │  │ Button (saveBtn)                 │  │
                        │  │ "저장하기"                        │──┼──▶ Firebase에 저장
                        │  └─────────────────────────────────┘  │
                        └───────────────────────────────────────┘
```

---

## 4. Firebase 데이터 구조

```
Firebase Realtime Database
│
└── myMemo/
    └── {uid}/                          ← FirebaseAuth 익명 사용자 UID
        ├── {push_key_1}/
        │   ├── date: "2026. 4. 7"
        │   └── memo: "런닝 30분"
        ├── {push_key_2}/
        │   ├── date: "2026. 4. 8"
        │   └── memo: "스쿼트 50회"
        └── ...
```

---

## 5. 클래스 다이어그램

```
┌─────────────────────────────────────┐
│          SplashActivity             │
│         (AppCompatActivity)         │
├─────────────────────────────────────┤
│ - auth: FirebaseAuth                │
├─────────────────────────────────────┤
│ + onCreate()                        │
│   - FirebaseAuth.getInstance()      │
│   - signInAnonymously()            │
│   - 3초 후 MainActivity 이동        │
└──────────────┬──────────────────────┘
               │ Intent
               ▼
┌─────────────────────────────────────┐
│          MainActivity               │
│         (AppCompatActivity)         │
├─────────────────────────────────────┤
│ + dataModelList: MutableList        │
│       <DataModel>                   │
├─────────────────────────────────────┤
│ + onCreate()                        │
│   - FirebaseDatabase.getInstance()  │
│   - getReference("myMemo")          │
│     .child(currentUser.uid)         │
│   - addValueEventListener()         │
│   - AlertDialog (메모 작성)          │
│   - DatePickerDialog (날짜 선택)     │
│   - myRef.push().setValue(model)    │
└──────────────┬──────────────────────┘
               │ 사용
               ▼
┌─────────────────────────────────────┐     ┌──────────────────────────┐
│        ListViewAdapter              │     │       DataModel          │
│          (BaseAdapter)              │     │      (data class)        │
├─────────────────────────────────────┤     ├──────────────────────────┤
│ + List: MutableList<DataModel>      │────▶│ + date: String = ""      │
├─────────────────────────────────────┤     │ + memo: String = ""      │
│ + getCount(): Int                   │     └──────────────────────────┘
│ + getItem(Int): Any?               │
│ + getItemId(Int): Long             │
│ + getView(): View                  │
│   - inflate(listview_item)         │
│   - listViewDateArea에 date 표시    │
│   - listViewMemoArea에 memo 표시    │
└─────────────────────────────────────┘
```

---

## 6. 레이아웃 ↔ 코드 매핑

| 레이아웃 파일 | 사용처 | 주요 View ID |
|---|---|---|
| `activity_splash.xml` | SplashActivity | `main` (ConstraintLayout), TextView("다이어트는 내일부터 아닙니까") |
| `activity_main.xml` | MainActivity | `main` (ConstraintLayout), `mainLV` (ListView), `writeBtn` (ImageView) |
| `custom_dialog.xml` | MainActivity → AlertDialog | `healthMemo` (EditText), `dateSelectBtn` (Button), `saveBtn` (Button) |
| `listview_item.xml` | ListViewAdapter → getView() | `listViewDateArea` (TextView), `listViewMemoArea` (TextView) |

---

## 7. 의존성 구조

```
┌──────────────────────────────────────────────────┐
│               Firebase BoM 34.11.0               │
│  ┌────────────────┬───────────────┬───────────┐  │
│  │ firebase-auth  │ firebase-     │ firebase- │  │
│  │                │ database      │ analytics │  │
│  └───────┬────────┴───────┬───────┴─────┬─────┘  │
└──────────┼────────────────┼─────────────┼────────┘
           │                │             │
           ▼                ▼             ▼
   SplashActivity    MainActivity    자동 수집
   (익명 로그인)     (DB 읽기/쓰기)

┌──────────────────────────────────────────────────┐
│                  AndroidX                        │
│  ┌───────────┬───────────┬────────────────────┐  │
│  │ appcompat │ activity  │ constraintlayout   │  │
│  │  1.7.1    │  1.13.0   │     2.2.1          │  │
│  └───────────┴───────────┴────────────────────┘  │
│  ┌───────────┬───────────────────────────────┐   │
│  │ core-ktx  │  material 1.13.0              │   │
│  │  1.18.0   │                               │   │
│  └───────────┴───────────────────────────────┘   │
└──────────────────────────────────────────────────┘
```

---

## 8. Gradle 플러그인 구조

```
root build.gradle.kts
├── com.android.application (AGP 9.1.0)     apply false
└── com.google.gms.google-services (4.4.4)  apply false

app/build.gradle.kts
├── com.android.application                 applied
└── com.google.gms.google-services          applied
    └── google-services.json 읽어서 Firebase 초기화 자동 처리
```

---

## 9. 액티비티 생명주기 흐름

```
[앱 시작]
    │
    ▼
SplashActivity.onCreate()
    │
    ├── FirebaseAuth.getInstance()
    │
    ├── try: currentUser.uid 확인 (이미 로그인됨)
    │   └── 3초 대기 → MainActivity로 이동 → finish()
    │
    └── catch: signInAnonymously() 호출
        ├── 성공 → 3초 대기 → MainActivity로 이동 → finish()
        └── 실패 → Toast "비회원 로그인 실패"

MainActivity.onCreate()
    │
    ├── FirebaseDatabase.getInstance()
    │   └── getReference("myMemo").child(uid)
    │
    ├── ListViewAdapter 생성 & ListView에 연결
    │
    ├── addValueEventListener (실시간 데이터 동기화)
    │   └── onDataChange → dataModelList 갱신 → notifyDataSetChanged()
    │
    └── writeBtn 클릭 →
        └── AlertDialog 표시
            ├── dateSelectBtn 클릭 → DatePickerDialog
            └── saveBtn 클릭 → DataModel 생성 → myRef.push().setValue(model)
```

---

## 10. 빌드 정보 요약

| 항목 | 값 |
|---|---|
| Package | `com.woonggon.diet_memo` |
| minSdk | 24 (Android 7.0) |
| targetSdk | 36 |
| compileSdk | 36 |
| Java 호환성 | Java 11 |
| AGP | 9.1.0 |
| Firebase BoM | 34.11.0 |
| Google Services Plugin | 4.4.4 |
