# wisesaying 프로젝트 문제 해결 기록

## 1. jlink 빌드 에러 (CRITICAL)

**에러 메시지:**
```
Execution failed for task ':app:compileDebugJavaWithJavac'.
> jlink executable ... does not exist.
```

**원인:** `gradle-daemon-jvm.properties`에 `toolchainVersion=21`로 설정되어 있으나, 시스템에 JDK 17(Corretto)만 설치되어 있어 JDK 21의 jlink를 찾지 못함.

**수정:** `gradle/gradle-daemon-jvm.properties`에서 `toolchainVersion=21` → `toolchainVersion=17`로 변경.

---

## 2. duplicate setContentView 호출

**문제:** `MainActivity.kt`에서 `setContentView(R.layout.activity_main)`과 `DataBindingUtil.setContentView()`를 중복 호출. `DataBindingUtil.setContentView()`가 내부적으로 `setContentView`를 호출하므로, 첫 번째 호출로 설정된 view hierarchy(window insets listener 포함)가 두 번째 호출로 교체되어 유실됨.

**수정:**
- `setContentView(R.layout.activity_main)` 제거
- `DataBindingUtil.setContentView()`만 사용
- `ViewCompat.setOnApplyWindowInsetsListener`를 binding 이후로 이동, `binding.main` 사용

```kotlin
// Before
setContentView(R.layout.activity_main)
ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { ... }
binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

// After
binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
ViewCompat.setOnApplyWindowInsetsListener(binding.main) { ... }
```

---

## 3. XML 레이아웃 constraint 문제 (activity_main.xml)

### 3-1. Button에 제약조건 없음

**문제:** `showAllSentenceBtn` 버튼에 `app:layout_constraint*` 속성이 없고, `tools:layout_editor_absoluteX/Y`만 있어 런타임에 (0, 0) 위치에 렌더링됨.

**수정:** `tools:layout_editor_absoluteX/Y` 제거, `constraintTop_toTopOf="parent"` + `constraintEnd_toEndOf="parent"` 추가.

### 3-2. TextView 충돌 제약조건

**문제:**
```xml
app:layout_constraintEnd_toEndOf="parent"
app:layout_constraintEnd_toStartOf="parent"  <!-- 오타 -->
```
`End_toStartOf="parent"`는 오른쪽 끝을 부모의 왼쪽(0)에 고정하므로 충돌 발생.

**수정:** `constraintEnd_toStartOf="parent"` → `constraintStart_toStartOf="parent"`로 변경하여 수평 중앙 정렬.

---

## 4. 중복 databinding-runtime 의존성

**문제:** `build.gradle.kts`에 `implementation(libs.androidx.databinding.runtime)` 수동 추가됨. `dataBinding { enable = true }` 설정 시 AGP가 자동으로 포함하므로 버전 충돌 위험.

**수정:** `implementation(libs.androidx.databinding.runtime)` 제거.

---

## 5. showButton → showAllSentenceBtn 참조 불일치

**에러 메시지:**
```
Unresolved reference 'showButton'.
```

**원인:** XML에서 버튼 id가 `showAllSentenceBtn`인데 Kotlin 코드에서 `binding.showButton`으로 참조.

**수정:** `binding.showButton` → `binding.showAllSentenceBtn`으로 변경.
