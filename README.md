
![github](https://github.com/sdhong0609/Parsing-Github-Repositories/assets/78577085/6a94374e-22f0-4ebd-9b29-26faf881f00c)

# 💡 Topic

- **GitHub Repository를 검색하고 간략한 정보를 보여주는 서비스**
- **GitHub에서 제공하는 Public API를 이용하여 개발**

<br>

# 📝 Summary
안드로이드 구성 요소에 대한 개념을 공부하고, 안드로이드 프로젝트 경험을 좀더 늘리고 싶어서 간단한 GitHub Repository 검색 앱을 만들었습니다. 6월 ~ 8월에는 다른 사이드프로젝트를 진행하게 되어서, 5월에 잠깐 진행하고 9월부터 다시 개발을 진행하였습니다. 멘토 분의 코드리뷰를 받으면서 진행하였습니다.

<br>

# ⭐️ Key Function
- **GitHub Repository 검색**
    - 사용자가 검색한 키워드에 따라 Repository 목록을 화면에 표시
    - 10개씩 아이템을 불러오고 스크롤하면 새로운 아이템이 나타나는 무한 스크롤 구현 (페이징 처리)
    - 모든 아이템을 불러오면 마지막 페이지라는 Toast 표시
- **Repository의 간략한 정보 표시**
    - 검색한 Repository 목록 중 아이템을 누르면 해당 Repository의 간략한 정보를 보여주는 화면 표시
- **Repository URL로 이동**
    - Repository 정보 화면에서 URL을 누르면 브라우저 앱을 통해 해당 Repository 페이지로 이동

<br>

# 🛠 Tech Stack
`Kotlin`, `RecyclerView`, `DataBinding`, `AAC ViewModel`, `LiveData`, `Retrofit`, `Coroutines`, `Hilt`, `Glide`, `OkHttp`, `Timber`

<br>

# ⚙️ Architecture
`MVVM`

<br>

# 🤚🏻 Part
- **안드로이드 앱 전체 개발**
<br>

# 🤔 Learned
- **MVC 패턴에서 MVVM 패턴으로** 마이그레이션하는 방법을 직접 경험하며 알게 되었습니다.
- `RecyclerView.Adapter` 대신, `ListAdapter` 를 사용하여 효율적으로 아이템을 업데이트하였고, `ListAdapter` 의 내부 동작 원리에 대해 공부하였습니다.
- 페이징 처리를 하여 **무한 스크롤**을 구현하는 방법을 알게 되었습니다.
- Kotlin의 확장 함수 기능과 `BindingAdapter` 를 사용하여 코드를 간결하게 만드는 법을 알게 되었습니다.
- `LiveData` 를 이용해서 Event를 처리할 때, Event가 중복되는 문제를 해결하기 위해 `Event Wrapper` 를 활용하였습니다.
- 데이터 모델을 API 응답 모델과 앱에서 사용하는 모델로 분리하면서, 유지보수에 관해 고민해보았습니다.
- 직렬화를 하면서 Serializable과 Parcelable의 차이점에 대해 공부해보았습니다.

<br>

# ✅ Problem Solving

📌 **RecyclerView 초기화 시 onScrolled() 함수 실행**
- RecyclerView 초기화 시 scroll listener의 `onScrolled()` 함수가 실행되어, 실제로는 스크롤을 하지 않았는데 스크롤을 했을 때와 동일하게 작동하는 문제가 발생함.
- `onScrolled()` 함수의 파라미터로 `dy: Int` 를 발견함. 이 값은 수직방향으로 실제 스크롤한 길이를 나타내는 값.
- `if (dy <= 0) return` 코드를 `onScrolled()` 최상단에 추가하여, 실제 스크롤이 발생하지 않으면 return하여 함수 진행을 종료시킴으로써 해결.

📌 **GitHub REST API 403 오류 발생**
- 구글링 결과 Personal Access Token을 HTTP Header에 추가해야 api를 원활하게 사용할 수 있다는 것을 알게 되었음. Token을 추가하지 않으면 api를 제한적으로만 사용 가능함.
- Token을 HTTP Header에 추가하였고 정상 작동함을 확인.
- 하지만 토큰은 개인 정보이기 때문에, commit 및 push하면 **GitHub에서 감지 후 토큰을 삭제해버리는 문제**가 발생하여 **현재 해결 시도중** → “Personal Access Token을 받아오는 REST API는 없을까?”

<br>

# 📷 Screenshot
|![Screenshot_20231016-171920_Parsing-Github-Repositories](https://github.com/sdhong0609/Parsing-Github-Repositories/assets/78577085/8e47c3d7-345d-48bc-835e-2555c8397da0) |![Screenshot_20231016-172831_Parsing-Github-Repositories](https://github.com/sdhong0609/Parsing-Github-Repositories/assets/78577085/5508c326-642b-48af-8702-c4f55a5ba918) |![Screenshot_20231016-172831_Parsing-Github-Repositories](https://github.com/sdhong0609/Parsing-Github-Repositories/assets/78577085/5508c326-642b-48af-8702-c4f55a5ba918)|
|-|-|-|

<br>

# 📺 Demo Video

<div align="center">
  <video src="https://github.com/sdhong0609/Parsing-Github-Repositories/assets/78577085/98a8e6d3-dda7-4de8-a162-8cda76bf355e" width="400" />
</div>


