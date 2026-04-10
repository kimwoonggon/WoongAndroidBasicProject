# RecyclerView - MainActivity와 CustomAdapter 상호작용 교육

## 1. 전체 구조 한눈에 보기

```
┌─────────────────────────────────────────────────┐
│  MainActivity                                    │
│                                                  │
│  1. 데이터 준비 (loadData)                        │
│  2. Adapter 생성 + 데이터 전달                     │
│  3. RecyclerView에 Adapter 연결                   │
│  4. LayoutManager 설정                            │
│                                                  │
│  ┌─────────────────────────────────────────────┐ │
│  │  RecyclerView (화면에 보이는 리스트)           │ │
│  │                                             │ │
│  │  ┌─────────────────────────────────────┐    │ │
│  │  │ item.xml (1번 아이템)                │    │ │
│  │  │  [1]  [이것이 안드로이드다 1]  [날짜]  │    │ │
│  │  └─────────────────────────────────────┘    │ │
│  │  ┌─────────────────────────────────────┐    │ │
│  │  │ item.xml (2번 아이템)                │    │ │
│  │  │  [2]  [이것이 안드로이드다 2]  [날짜]  │    │ │
│  │  └─────────────────────────────────────┘    │ │
│  │  ...                                        │ │
│  └─────────────────────────────────────────────┘ │
│          ▲                                       │
│          │ Adapter가 데이터를 화면에 연결           │
│  ┌───────┴─────────────────────────────────────┐ │
│  │  CustomAdapter                              │ │
│  │  listData = [Memo1, Memo2, ..., Memo100]    │ │
│  └─────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────┘
```

---

## 2. 비유로 이해하기: 식당

| 식당 | RecyclerView |
|------|-------------|
| 사장님 (전체 관리) | **MainActivity** |
| 메뉴판 데이터 | **MutableList\<Memo\>** |
| 웨이터 (데이터→테이블) | **CustomAdapter** |
| 테이블 한 줄 | **item.xml** (아이템 레이아웃) |
| 테이블 위 접시 세트 | **Holder** (View를 담는 그릇) |
| 식당 홀 전체 | **RecyclerView** |
| 테이블 배치 방식 | **LayoutManager** (세로/가로/격자) |

> **사장님(MainActivity)**이 메뉴 데이터를 준비해서 **웨이터(Adapter)**에게 넘기면,
> 웨이터가 **테이블(item.xml)**에 음식을 **접시(Holder)**에 담아 올려놓는 것!

---

## 3. 코드 흐름 (순서대로)

### Step 1: 데이터 준비 — `MainActivity.loadData()`

```kotlin
fun loadData(): MutableList<Memo> {
    val data: MutableList<Memo> = mutableListOf()
    for (no in 1..100) {
        val title = "이것이 안드로이드다 ${no}"
        val date = System.currentTimeMillis()
        val memo = Memo(no, title, date)  // Memo 객체 생성
        data.add(memo)
    }
    return data  // 100개의 Memo 리스트 반환
}
```

**역할:** 화면에 보여줄 데이터 100개를 만든다.

---

### Step 2: Adapter에 데이터 전달 — `MainActivity.onCreate()`

```kotlin
val data = loadData()              // ① 데이터 100개 준비
val adapter = CustomAdapter()      // ② Adapter 생성
adapter.listData = data            // ③ 데이터를 Adapter에 넘김
```

이 시점에서 Adapter는 데이터를 **보관만** 하고 있다. 아직 화면에는 아무것도 안 나온다.

---

### Step 3: RecyclerView에 Adapter 연결

```kotlin
val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
recyclerView.adapter = adapter                        // ④ Adapter 연결
recyclerView.layoutManager = LinearLayoutManager(this) // ⑤ 세로 스크롤 설정
```

**이 순간부터 RecyclerView가 Adapter에게 질문을 시작한다!**

---

### Step 4: RecyclerView → Adapter 질문 3가지

RecyclerView가 Adapter에게 하는 질문은 딱 **3가지**:

```
RecyclerView: "아이템 몇 개야?"
  → Adapter:  getItemCount() 호출  →  return 100

RecyclerView: "아이템 틀(View) 하나 만들어줘"
  → Adapter:  onCreateViewHolder() 호출  →  item.xml을 inflate해서 Holder 반환

RecyclerView: "이 Holder에 n번째 데이터 채워줘"
  → Adapter:  onBindViewHolder() 호출  →  Holder에 Memo 데이터 세팅
```

---

### Step 5: 각 함수의 역할 상세

#### `getItemCount()` — "몇 개?"

```kotlin
override fun getItemCount(): Int {
    return listData.size  // 100
}
```

RecyclerView가 스크롤 범위를 계산하기 위해 호출한다.

---

#### `onCreateViewHolder()` — "틀 만들어"

```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
    val itemView = LayoutInflater.from(parent.context)
        .inflate(R.layout.item, parent, false)  // item.xml → View 객체로 변환
    return Holder(itemView)                      // Holder로 감싸서 반환
}
```

```
item.xml 파일                     View 객체                    Holder
┌──────────────┐    inflate     ┌──────────────┐   감싸기   ┌──────────────┐
│ <LinearLayout>│ ──────────▶  │  실제 View    │ ────────▶ │  Holder      │
│   <TextView/> │              │  (메모리에)    │           │  .itemView   │
│   <TextView/> │              └──────────────┘           └──────────────┘
│   <TextView/> │
└──────────────┘
```

> **핵심:** 100개 아이템이 있어도 이 함수는 **화면에 보이는 개수 + α** 만큼만 호출된다 (예: 15번).
> 나머지는 **재활용(Recycle)**한다! → 이게 Recycler**View** 이름의 유래.

---

#### `onBindViewHolder()` — "데이터 채워"

```kotlin
override fun onBindViewHolder(holder: Holder, position: Int) {
    val memo = listData.get(position)  // position번째 Memo 가져오기
    holder.setMemo(memo)                // Holder에 데이터 세팅
}
```

```
position = 0  →  listData[0] = Memo(1, "이것이 안드로이드다 1", 날짜)
                  → holder.setMemo(memo)
                  → textNo = "1", textTitle = "이것이..1", textDate = "2026/04/11"

position = 1  →  listData[1] = Memo(2, "이것이 안드로이드다 2", 날짜)
                  → holder.setMemo(memo)
                  → textNo = "2", textTitle = "이것이..2", textDate = "2026/04/11"
```

---

#### `Holder.setMemo()` — "View에 실제 값 넣기"

```kotlin
class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setMemo(memo: Memo) {
        itemView.findViewById<TextView>(R.id.textNo).text = "${memo.no}"
        itemView.findViewById<TextView>(R.id.textTitle).text = memo.title

        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        itemView.findViewById<TextView>(R.id.textDate).text = sdf.format(memo.timestamp)
    }
}
```

> Holder는 item.xml의 View들을 찾아서 Memo 데이터를 **실제로 화면에 표시**하는 역할.

---

## 4. 재활용(Recycling) 이해하기

이것이 RecyclerView의 **핵심 개념**:

```
화면에 보이는 영역
┌──────────────────────┐
│  아이템 1  ← Holder A │  ▲ 스크롤 위로 사라짐
│  아이템 2  ← Holder B │
│  아이템 3  ← Holder C │
│  아이템 4  ← Holder D │
│  아이템 5  ← Holder E │
└──────────────────────┘
   아이템 6  (아직 안 보임)

         ↓ 스크롤 다운

┌──────────────────────┐
│  아이템 2  ← Holder B │
│  아이템 3  ← Holder C │
│  아이템 4  ← Holder D │
│  아이템 5  ← Holder E │
│  아이템 6  ← Holder A │  ← 위에서 사라진 Holder A를 재활용!
└──────────────────────┘
```

- 아이템 1이 화면 위로 사라지면, 그 **Holder A**는 버리지 않고
- 아래에서 새로 나타나는 아이템 6에 **재활용**한다
- 이때 `onBindViewHolder(holderA, position=5)`가 호출되어 새 데이터를 채움
- **`onCreateViewHolder`는 다시 호출하지 않는다!** → 성능 향상

---

## 5. 전체 호출 시퀀스 다이어그램

```
MainActivity                  RecyclerView              CustomAdapter              Holder
    │                              │                          │                      │
    │  recyclerView.adapter = adapter                         │                      │
    │──────────────────────────▶│                              │                      │
    │                           │  getItemCount()?            │                      │
    │                           │─────────────────────────▶  │                      │
    │                           │  return 100                 │                      │
    │                           │◀─────────────────────────  │                      │
    │                           │                             │                      │
    │                           │  onCreateViewHolder()       │                      │
    │                           │─────────────────────────▶  │                      │
    │                           │  return Holder(view)        │  new Holder(view)    │
    │                           │◀─────────────────────────  │─────────────────────▶│
    │                           │                             │                      │
    │                           │  onBindViewHolder(holder,0) │                      │
    │                           │─────────────────────────▶  │                      │
    │                           │                             │  holder.setMemo()    │
    │                           │                             │─────────────────────▶│
    │                           │                             │  (View에 값 세팅)     │
    │                           │                             │◀─────────────────────│
    │                           │                             │                      │
    │                           │  (반복: 화면에 보이는 만큼)   │                      │
    │                           │  ...                        │                      │
```

---

## 6. 파일별 역할 정리

| 파일 | 역할 | 한 줄 요약 |
|------|------|-----------|
| **Memo.kt** | 데이터 클래스 | 아이템 하나의 정보 (번호, 제목, 날짜) |
| **MainActivity.kt** | 컨트롤러 | 데이터 준비 → Adapter 생성 → RecyclerView 연결 |
| **CustomAdapter.kt** | 중간 다리 | 데이터 ↔ View 연결. 3개 함수 구현 필수 |
| **Holder** | View 보관함 | item.xml의 View 참조를 들고 있음 |
| **activity_main.xml** | 메인 화면 | RecyclerView 배치 |
| **item.xml** | 아이템 한 줄 | 번호, 제목, 날짜 TextView 3개 |

---

## 7. 자주 하는 실수 (이번에 고친 것들)

| 실수 | 왜 문제인가 | 해결 |
|------|-----------|------|
| `parent` 대신 `p0` 파라미터명 | 이름이 다르면 컴파일 에러 | 파라미터명을 의미있게 변경 |
| `itemView.textNo` (Kotlin synthetic) | deprecated + 플러그인 필요 | `findViewById<TextView>(R.id.textNo)` 사용 |
| activity_main.xml에 RecyclerView 없음 | 화면에 리스트가 안 나옴 | RecyclerView 위젯 추가 |
| Layout ID와 코드 ID 불일치 | `findViewById`가 null 반환 → 크래시 | ID 통일 (`textNo`, `textTitle`, `textDate`) |

---

## 8. 심화: MainActivity → CustomAdapter → Holder 데이터 여행기

데이터가 **어디서 태어나서, 어디를 거쳐, 최종적으로 화면에 나오는지** 하나하나 따라가 보자.

### 8-1. 데이터의 탄생 (MainActivity)

```kotlin
// MainActivity.kt
val data = loadData()   // Memo 100개짜리 리스트 생성
```

이 시점에 메모리에는 이런 리스트가 있다:

```
data = [
    Memo(no=1,  title="이것이 안드로이드다 1",  timestamp=1712819400000),
    Memo(no=2,  title="이것이 안드로이드다 2",  timestamp=1712819400001),
    ...
    Memo(no=100, title="이것이 안드로이드다 100", timestamp=1712819400099)
]
```

### 8-2. Adapter에게 데이터 건네기

```kotlin
// MainActivity.kt
val adapter = CustomAdapter()
adapter.listData = data    // ← 여기서 데이터가 Adapter로 넘어감
```

이건 **복사가 아니라 참조 전달**이다.
즉 MainActivity의 `data`와 Adapter의 `listData`는 **같은 리스트를 가리킨다**.

```
MainActivity.data ──────┐
                        ▼
                   [ Memo1, Memo2, ..., Memo100 ]  ← 메모리에 하나만 존재
                        ▲
CustomAdapter.listData ─┘
```

### 8-3. RecyclerView가 Adapter를 작동시키는 순간

```kotlin
// MainActivity.kt
recyclerView.adapter = adapter   // ← 이 한 줄이 모든 것을 시작시킨다!
```

이 줄이 실행되면 RecyclerView는 **자동으로** Adapter의 함수들을 호출하기 시작한다.
우리가 직접 호출하는 게 아니다! RecyclerView 프레임워크가 알아서 한다.

---

### 8-4. onCreateViewHolder — "빈 껍데기 만들기"

```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
    val itemView = LayoutInflater.from(parent.context)
        .inflate(R.layout.item, parent, false)
    return Holder(itemView)
}
```

#### 이 함수가 하는 일

**item.xml 파일을 읽어서 실제 View 객체로 만든다.** 이걸 "inflate(부풀리기)"라고 한다.

```
item.xml (설계도)                      실제 View (건물)
┌─────────────────────┐   inflate    ┌─────────────────────┐
│ <LinearLayout>       │ ─────────▶ │ LinearLayout 객체     │
│   <TextView "00"/>   │            │   ├ TextView 객체     │
│   <TextView "Title"/>│            │   ├ TextView 객체     │
│   <TextView "날짜"/> │            │   └ TextView 객체     │
│ </LinearLayout>      │            └─────────────────────┘
└─────────────────────┘
```

> **비유:** item.xml은 아파트 **설계도**이고, inflate는 설계도를 보고 실제 **아파트를 짓는 것**.
> Holder는 그 아파트의 **열쇠 묶음** (각 방(TextView)에 접근할 수 있는 참조를 들고 있음).

#### 파라미터 설명

| 파라미터 | 의미 |
|---------|------|
| `parent` | RecyclerView 자체. "이 아이템이 들어갈 부모 View가 뭐야?" |
| `parent.context` | Activity(화면) 정보. inflate할 때 필요 |
| `R.layout.item` | 어떤 설계도(xml)를 쓸 건지 |
| `false` | 지금 당장 parent에 붙이지 마라 (RecyclerView가 나중에 알아서 붙임) |

#### 핵심 포인트: 이 함수는 몇 번 호출될까?

아이템이 100개라도 **화면에 동시에 보이는 개수 + 여유분**만큼만 호출된다!

```
화면 크기가 아이템 5개를 보여줄 수 있다면:
→ onCreateViewHolder()는 약 7~8번만 호출 (5개 + 위아래 여유 2~3개)
→ 나머지 92~93개는 기존 Holder를 재활용
```

**왜?** View를 만드는 건 비싸다(느리다). 100번 만드는 대신 7번만 만들고 돌려쓰면 빠르다!

---

### 8-5. Holder — "View에 접근하는 리모컨"

```kotlin
class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setMemo(memo: Memo) {
        itemView.findViewById<TextView>(R.id.textNo).text = "${memo.no}"
        itemView.findViewById<TextView>(R.id.textTitle).text = memo.title

        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = sdf.format(memo.timestamp)
        itemView.findViewById<TextView>(R.id.textDate).text = date
    }
}
```

#### Holder가 뭔데?

Holder = **View를 "hold(잡고있다)"하는 객체**

아까 `onCreateViewHolder`에서 만든 View(아파트)의 **열쇠 묶음**이다.
Holder가 있으면 item.xml 안의 TextView들에 언제든 접근할 수 있다.

```
Holder 객체
┌─────────────────────────────────┐
│  .itemView = LinearLayout       │
│       ├─ textNo    (TextView)   │  ← findViewById로 접근
│       ├─ textTitle (TextView)   │  ← findViewById로 접근
│       └─ textDate  (TextView)   │  ← findViewById로 접근
│                                 │
│  .setMemo(memo) 메서드           │  ← 데이터를 View에 세팅하는 함수
└─────────────────────────────────┘
```

#### 왜 Holder가 필요한가?

**Holder 없이** 매번 이렇게 해야 한다면:
```kotlin
// 매번 View 전체에서 TextView를 찾아야 함 → 느림!
someView.findViewById<TextView>(R.id.textNo).text = "1"
someView.findViewById<TextView>(R.id.textTitle).text = "제목"
```

**Holder를 개선해서** 이렇게 할 수도 있다 (참고):
```kotlin
class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // 미리 한 번만 찾아두면 나중에 빠르게 접근 가능
    val textNo = itemView.findViewById<TextView>(R.id.textNo)
    val textTitle = itemView.findViewById<TextView>(R.id.textTitle)
    val textDate = itemView.findViewById<TextView>(R.id.textDate)

    fun setMemo(memo: Memo) {
        textNo.text = "${memo.no}"        // findViewById 안 해도 됨!
        textTitle.text = memo.title
        textDate.text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            .format(memo.timestamp)
    }
}
```

> 이렇게 하면 `findViewById`는 Holder가 만들어질 때 **딱 한 번**만 호출되고,
> `setMemo`가 100번 호출되어도 **이미 찾아둔 참조를 재사용**한다. 더 빠르다!

---

### 8-6. onBindViewHolder — "빈 껍데기에 데이터 채우기"

```kotlin
override fun onBindViewHolder(holder: Holder, position: Int) {
    val memo = listData.get(position)  // position번째 데이터 꺼내기
    holder.setMemo(memo)                // Holder에 데이터 세팅
}
```

#### 이 함수가 하는 일

`onCreateViewHolder`가 **빈 아파트를 지었다면**,
`onBindViewHolder`는 그 아파트에 **가구를 배치하는 것**이다.

```
 Holder (빈 아파트)                   데이터 (가구)                    결과
┌──────────────────┐            ┌──────────────────┐          ┌──────────────────┐
│  textNo = ""      │            │  memo.no = 3      │          │  textNo = "3"     │
│  textTitle = ""   │  + bind +  │  memo.title =     │   =      │  textTitle =      │
│  textDate = ""    │            │  "이것이..3"       │          │  "이것이..3"       │
└──────────────────┘            │  memo.timestamp   │          │  textDate =       │
                                │  = 171281940002   │          │  "2026/04/11"     │
                                └──────────────────┘          └──────────────────┘
```

#### 파라미터 설명

| 파라미터 | 의미 | 예시 |
|---------|------|------|
| `holder` | 데이터를 채울 Holder (빈 아파트 or 재활용 아파트) | 위에서 만든 Holder 객체 |
| `position` | 지금 채워야 할 아이템 번호 (0부터 시작) | 0, 1, 2, ..., 99 |

#### Create vs Bind 호출 횟수 비교

```
아이템 100개, 화면에 5개 보임:

onCreateViewHolder() 호출:  ████████ (약 7~8번)
onBindViewHolder()   호출:  ████████████████████████████████████████ (스크롤할 때마다)

스크롤하면:
→ onCreate는 추가 호출 안 됨 (이미 만든 Holder 재활용)
→ onBind만 새 position으로 계속 호출됨
```

---

### 8-7. 전체 데이터 여행 정리

```
[1단계] MainActivity.loadData()
    │
    │  Memo 100개 생성
    ▼
[2단계] adapter.listData = data
    │
    │  Adapter에게 리스트 참조 전달
    ▼
[3단계] recyclerView.adapter = adapter  ← RecyclerView가 자동으로 아래를 실행
    │
    ├──▶ getItemCount() → 100개라고 알려줌
    │
    ├──▶ onCreateViewHolder() × 7~8번
    │       │
    │       │  item.xml → inflate → View 생성 → Holder로 감싸기
    │       ▼
    │    Holder 7~8개 생성됨 (빈 아파트)
    │
    └──▶ onBindViewHolder(holder, position) × 화면에 보이는 만큼
            │
            │  listData[position] → memo 꺼냄
            │  holder.setMemo(memo) → View에 데이터 표시
            ▼
         화면에 아이템 표시됨!

[스크롤 시]
    │
    ├──▶ onCreateViewHolder() → 호출 안 됨! (재활용)
    │
    └──▶ onBindViewHolder(재활용Holder, 새position)
            │
            │  기존 Holder에 새 데이터만 갈아끼움
            ▼
         새 아이템 표시됨!
```

---

### 8-8. 한 줄 요약

| 개념 | 비유 | 실제 역할 |
|------|------|----------|
| **onCreateViewHolder** | 빈 아파트 짓기 | item.xml → View → Holder 생성 |
| **Holder** | 아파트 열쇠 묶음 | View의 참조를 들고 있음 |
| **onBindViewHolder** | 가구 배치하기 | Holder의 View에 Memo 데이터 세팅 |
| **Create는 적게, Bind는 많이** | 아파트는 몇 채만 짓고, 이사만 자주 | 성능 최적화의 핵심 |
