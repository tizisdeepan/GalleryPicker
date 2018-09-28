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

## [5] If you don't want to use the Picker Activity, you this to fetch Photos and Videos to use them as you wish
```kotlin
val images = GalleryPicker(this).getImages()
val videos = GalleryPicker(this).getVideos()
Log.e("RESULT", "IMAGES COUNT: ${images.size}\nVIDEOS COUNT: ${videos.size}")
```

# Documentation
## GalleryImage Model
|Property|Description|
|---|---|
|ID [String]|**Unique Identifier** for the image|
|DATA [String]|**Uri** of the image|
|DATE_ADDED [Long]|**Date (timeInMillis)** of when the image has been **Added**|
|DATE_MODIFIED [Long]|**Date (timeInMillis)** of when the image has been **Last Modified**|
|DATE_TAKEN [Long]|**Date (timeInMillis)** of when the image has been **Captured**|
|DISPLAY_NAME [String]|**Name** of the image|
|HEIGHT [String]|**Height** of the image in pixels|
|WIDTH [String]|**Width** of the image in pixels|
|MIME_TYPE [String]|**Mime Type** of the image|
|SIZE [String]|**Size** of the image in bytes|
|TITLE [String]|**Title** of the Image|
|BUCKET_DISPLAY_NAME [String]|**Bucket Name** of the image in Media Store|
|BUCKET_ID [String]|**Bucket ID** of the image in Media Store|
|DESCRIPTION [String]|**Decription** of the image|
|IS_PRIVATE [String]|Tells whether the image is in **Private Directory** or not|
|LATITUDE [String]|**Location** coordinates - Latitude|
|LONGITUDE [String]|**Location** coordinates - Longitude|
|MINI_THUMB_MAGIC [String]|**Mini Thumb Magic** property from Media Store|
|ORIENTATION [String]|**Orientation** type - Exif property|
|PICASA_ID [String]|**Picasa ID** property from Media Store|
|ALBUM_NAME [String]|**Name of the Album** in which the image is present|

