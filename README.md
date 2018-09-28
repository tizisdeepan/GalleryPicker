# Gallery Picker
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://jitpack.io/v/tizisdeepan/gallerypicker.svg)](https://jitpack.io/#tizisdeepan/gallerypicker)

## What is Gallery Picker?
Gallery Picker allows you to design a custom gallery for image/ video picker in your android projects. You can also use the in-built cutom fragment developed with the help of the utility methods provided in the library. The utility methods include fetching data from the android's media store efficiently and album separations.

![Screenshot 2](https://github.com/tizisdeepan/gallerypicker/blob/master/Screenshots/ss.png)

## Implementation
### [1] In your app module gradle file
```gradle
dependencies {
    implementation 'com.github.tizisdeepan:gallerypicker:1.0.0'
}
```

### [2] In your project level gradle file
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

## [3] In your manifest.xml
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
NOTE: Make sure to implement run time permissions request in your app.

## [4] In your project, you can start PickerActivity by adding this line
```kotlin
startActivity(Intent(this@MainActivity, PickerActivity::class.java))
```
