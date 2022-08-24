# benshi.ai Test
Mobile App Developer Test

See [REST API project](https://github.com/typicode/json-server) here 

## 🚀 Technology Stack

- Android
- Kotlin

## 📸 Screenshots
|                           |                            |
| :----------------------------------: | :----------------------------------: |
| ![Splash Screen](https://user-images.githubusercontent.com/22020160/186496432-ab451a42-9ceb-4311-8b3e-1c8ca721b36a.png) | ![Home Page](https://user-images.githubusercontent.com/22020160/186496592-5801afb6-df8a-4014-8e8f-37979d2893fb.png) |
| ![Post Details Screen](https://user-images.githubusercontent.com/22020160/186496718-3a7b62ff-261b-4e87-8b03-a586cd64f024.png) | ![Comment Section](https://user-images.githubusercontent.com/22020160/186496935-8f203da7-bd57-49b2-949e-b88b1f9dc1c4.png) |
| ![Add/Edit entry](https://user-images.githubusercontent.com/22020160/186497062-96633cde-9a0b-4804-8ea5-c3fc45a7351a.png) |


This app use collection of the [Architecture Components](https://developer.android.com/arch):

- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [Lifecycle-aware components](https://developer.android.com/topic/libraries/architecture/lifecycle)
- [ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Paging](https://developer.android.com/topic/libraries/architecture/paging/)
- [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/)
- [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager/)

## Rquirement 
[Google api key](https://developers.google.com/maps/documentation/javascript/get-api-key) is required to get author location map maker 
#### Kotlin
```kotlin
binding.mapImageView.load("${Constants.STATIC_MAP}center=${it.user.address.geo.lat}%2c%20${it.user.address.geo.lng}&zoom=12&size=400x400&key=${Constants.API_KEY}"){
                crossfade(true)
                crossfade(750)
                placeholder(R.drawable.ic_baseline_image_24)
                scale(Scale.FILL)
            }
```

## 📖 Things to doto 
There is still room for improvement
- [ ] Send mail with javamail-android
- [ ] Add tests (Yeah, I know :wink:)
- [ ] Handle Location


