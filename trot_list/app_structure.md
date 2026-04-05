# trot_list 앱 구조 설명

## 1. 앱 전체 흐름

```
앱 실행
  → MainActivity (activity_main.xml)
    → FragmentContainerView (NavHostFragment)
      → Navigation Graph (main_nav.xml) 에 의해 시작 Fragment 결정
        → Singer1Fragment (시작 화면)
```

---

## 2. 클래스 & 파일 목록

| 클래스명 | 파일 경로 | 역할 |
|---------|----------|------|
| `MainActivity` | `java/.../MainActivity.kt` | 앱의 진입점. `activity_main.xml`을 로드하고, 그 안의 `NavHostFragment`가 navigation을 관리 |
| `Singer1Fragment` | `java/.../Singer1Fragment.kt` | **영탁** 노래 리스트 화면 (시작 화면) |
| `Singer2Fragment` | `java/.../Singer2Fragment.kt` | **영웅** 노래 리스트 화면 |
| `Singer3Fragment` | `java/.../Singer3Fragment.kt` | **가인** 노래 리스트 화면 |
| `RVAdapter` | `java/.../RVAdapter.kt` | RecyclerView 어댑터. 노래 목록 데이터를 리스트 아이템으로 변환 |

---

## 3. 레이아웃 파일 목록

| XML 파일 | 사용처 | 내용 |
|---------|--------|------|
| `activity_main.xml` | `MainActivity` | `FragmentContainerView` 하나만 존재. Navigation Graph를 연결 |
| `fragment_singer1.xml` | `Singer1Fragment` | 제목("영탁노래 리스트") + RecyclerView + 하단 이미지 3개 |
| `fragment_singer2.xml` | `Singer2Fragment` | 제목("영웅노래 리스트") + RecyclerView + 하단 이미지 3개 |
| `fragment_singer3.xml` | `Singer3Fragment` | 제목("가인노래 리스트") + RecyclerView + 하단 이미지 3개 |
| `rv_item.xml` | `RVAdapter` | RecyclerView 한 줄 아이템. `TextView` (id: `rvTextId`) 하나 |
| `main_nav.xml` | Navigation Graph | Fragment 간 이동 경로(action) 정의 |

---

## 4. 화면 구성 (각 Fragment 공통)

```
┌─────────────────────────┐
│    제목 (TextView)        │  ← "영탁노래 리스트" 등
├─────────────────────────┤
│                         │
│   RecyclerView (singRV) │  ← 노래1, 노래2, 노래3 ... 세로 스크롤
│                         │
│                         │
├─────────────────────────┤
│ [photo1] [photo2] [photo3] │  ← 하단 가수 사진 3개 (ImageView)
│  image1   image2   image3  │     각각 클릭 시 해당 가수 Fragment로 이동
└─────────────────────────┘
```

---

## 5. 데이터 흐름 (RecyclerView)

```
Singer1Fragment.onCreateView()
  │
  ├─ 1) mutableListOf<String>() 로 노래 목록 생성
  │     → "노래1", "노래2", "노래3" 추가
  │
  ├─ 2) RVAdapter(items) 생성
  │
  ├─ 3) rv.adapter = rvAdapter 로 RecyclerView에 연결
  │
  └─ 4) rv.layoutManager = LinearLayoutManager(context) 로 세로 리스트 설정
```

**RVAdapter 내부 동작:**
1. `onCreateViewHolder()` → `rv_item.xml`을 inflate하여 ViewHolder 생성
2. `onBindViewHolder()` → ViewHolder의 `bindItems(item)` 호출
3. `bindItems()` → `itemView.findViewById<TextView>(R.id.rvTextId)`로 텍스트뷰를 찾아 노래 이름 세팅

---

## 6. Fragment 간 이동 원리 (Navigation)

### 6-1. Navigation Graph (`main_nav.xml`)

시작 화면은 `Singer1Fragment`이다 (`app:startDestination="@id/singer1Fragment"`).

각 Fragment에서 다른 Fragment로 이동하는 **action**이 정의되어 있다:

| 출발 Fragment | Action ID | 도착 Fragment |
|--------------|-----------|--------------|
| Singer1 | `action_singer1Fragment_to_singer2Fragment` | Singer2 |
| Singer1 | `action_singer1Fragment_to_singer3Fragment` | Singer3 |
| Singer2 | `action_singer2Fragment_to_singer1Fragment` | Singer1 |
| Singer2 | `action_singer2Fragment_to_singer3Fragment` | Singer3 |
| Singer3 | `action_singer3Fragment_to_singer1Fragment` | Singer1 |
| Singer3 | `action_singer3Fragment_to_singer2Fragment` | Singer2 |

### 6-2. 이미지 클릭 → Fragment 이동 코드

각 Fragment의 하단에 가수 사진 3장(`image1`, `image2`, `image3`)이 있다.
**자기 자신 사진은 클릭 이벤트가 없고**, 다른 2명의 사진에만 클릭 리스너가 붙어 있다.

예시 (Singer1Fragment):
```kotlin
// image2 (영웅 사진) 클릭 → Singer2Fragment로 이동
val image2 = view.findViewById<ImageView>(R.id.image2)
image2.setOnClickListener {
    it.findNavController().navigate(R.id.action_singer1Fragment_to_singer2Fragment)
}

// image3 (가인 사진) 클릭 → Singer3Fragment로 이동
val image3 = view.findViewById<ImageView>(R.id.image3)
image3.setOnClickListener {
    it.findNavController().navigate(R.id.action_singer1Fragment_to_singer3Fragment)
}
```

**동작 원리:**
1. `ImageView`에 `setOnClickListener` 등록
2. 클릭 시 `findNavController()` → 현재 Fragment가 속한 `NavController`를 가져옴
3. `.navigate(R.id.action_xxx)` → `main_nav.xml`에 정의된 action ID로 이동
4. `NavHostFragment`가 현재 Fragment를 교체하여 새 화면 표시

### 6-3. 이미지-가수 매핑

| 이미지 ID | drawable | 가수 | 이동 대상 Fragment |
|----------|----------|------|-------------------|
| `image1` | `photo1` | 가수1 (영탁) | `Singer1Fragment` |
| `image2` | `photo2` | 가수2 (영웅) | `Singer2Fragment` |
| `image3` | `photo3` | 가수3 (가인) | `Singer3Fragment` |

---

## 7. 연결 관계 요약도

```
MainActivity
  └── activity_main.xml
        └── FragmentContainerView (NavHostFragment)
              └── main_nav.xml (Navigation Graph)
                    ├── Singer1Fragment ← fragment_singer1.xml
                    │     ├── RecyclerView ← RVAdapter ← rv_item.xml
                    │     ├── image2 클릭 → Singer2Fragment
                    │     └── image3 클릭 → Singer3Fragment
                    │
                    ├── Singer2Fragment ← fragment_singer2.xml
                    │     ├── RecyclerView ← RVAdapter ← rv_item.xml
                    │     ├── image1 클릭 → Singer1Fragment
                    │     └── image3 클릭 → Singer3Fragment
                    │
                    └── Singer3Fragment ← fragment_singer3.xml
                          ├── RecyclerView ← RVAdapter ← rv_item.xml
                          ├── image1 클릭 → Singer1Fragment
                          └── image2 클릭 → Singer2Fragment
```
