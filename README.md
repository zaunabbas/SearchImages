<h1 align="center">Search Images App</h1>
 
A simple Search Images app using [Pixabay API](https://pixabay.com/api/docs/#api_search_images) with modern Android tech-stacks and MVVM architecture. Fetching data from the network and cache it offline.


## Download
Go to the [Download Link](https://drive.google.com/file/d/1pMLE4lVdZmeNS1OwjRtPZj5nxvb67LRa/view?usp=sharing) to download the latest APK.

## Screenshots
<p align="center">
<img src="/preview/preview0.png" width="32%"/>
<img src="/preview/preview1.png" width="32%"/>
<img src="/preview/preview2.png" width="32%"/>
<img src="/preview/preview3.png" width="32%"/>
<img src="/preview/preview4.png" width="32%"/>
<img src="/preview/preview5.png" width="32%"/>
</p>

## Tech stack & Open-source libraries
- Minimum SDK level 23 (Android 6.0)
- 100% [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Hilt for dependency injection.
- JetPack
  - Lifecycle - dispose observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Room - Used to Persist data.
  - Paging3 - load and display pages of data from a larger dataset.
- Architecture
  - MVVM Architecture (View - ViewModel - Model)
  - Repository pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
- [Sandwich](https://github.com/skydoves/Sandwich) - construct lightweight http API response and handling error responses.
