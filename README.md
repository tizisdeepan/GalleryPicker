# Gallery Picker
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://jitpack.io/v/tizisdeepan/gallerypicker.svg)](https://jitpack.io/#tizisdeepan/gallerypicker)
[![](https://jitpack.io/v/tizisdeepan/gallerypicker/month.svg)](https://jitpack.io/#tizisdeepan/gallerypicker)

## What is Gallery Picker?
Gallery Picker allows you to design a custom gallery for image/ video picker in your android projects. You can also use the in-built cutom fragment developed with the help of the utility methods provided in the library. The utility methods include fetching data from the android's media store efficiently and album separations.

![Screenshot 2](https://github.com/tizisdeepan/gallerypicker/blob/master/Screenshots/ss.png)

## Implementation
### [1] In your app module gradle file
```gradle
dependencies {
    implementation 'com.github.tizisdeepan:gallerypicker:1.0.1'
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
<uses-permission android:name="android.permission.CAMERA"/>
```
NOTE: Make sure to implement run time permissions request in your app.

## [4] In your project, you can start PickerActivity by adding this line
```kotlin
startActivity(Intent(this@MainActivity, PickerActivity::class.java))
```

## [5] You can define Picker Limits via your Intent
```kotlin
val i = Intent(this@MainActivity, PickerActivity::class.java)
i.putExtra("IMAGES_LIMIT", 7) // Allows you to pick 7 images
i.putExtra("VIDEOS_LIMIT", 7) // Allows you to pick 7 videos
startActivity(i)
```

## [6] If you don't want to use the PickerActivity, you this to fetch Photos and Videos
```kotlin
val images = GalleryPicker(context).getImages()
val videos = GalleryPicker(context).getVideos()
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

## GalleryVideo Model
|Property|Description|
|---|---|
|ID [String]|**Unique Identifier** for the video|
|DATA [String]|**Uri** of the video|
|DATE_ADDED [Long]|**Date (timeInMillis)** of when the video has been **Added**|
|DATE_MODIFIED [Long]|**Date (timeInMillis)** of when the video has been **Last Modified**|
|DATE_TAKEN [Long]|**Date (timeInMillis)** of when the video has been **Captured**|
|DISPLAY_NAME [String]|**Name** of the video|
|HEIGHT [String]|**Height** of the video in pixels|
|WIDTH [String]|**Width** of the video in pixels|
|MIME_TYPE [String]|**Mime Type** of the video|
|SIZE [String]|**Size** of the video in bytes|
|TITLE [String]|**Title** of the video|
|ALBUM [String]|**Album Name** of the video in Media Store|
|ARTIST [String]|**Artist Name** of the video in Media Store|
|BOOKMARK [String]|**Bookmark** of the video in Media Store|
|BUCKET_DISPLAY_NAME [String]|**Bucket Name** of the video in Media Store|
|BUCKET_ID [String]|**Bucket ID** of the video in Media Store|
|CATEGORY [String]|**Category** of the video|
|DESCRIPTION [String]|**Description** of the video|
|DURATION [String]|**Duration** of the video in milli seconds|
|IS_PRIVATE [String]|Tells whether the video is in **Private Directory** or not|
|LANGUAGE [String]|**Language** of the video|
|LATITUDE [String]|**Location** coordinates - Latitude|
|LONGITUDE [String]|**Location** coordinates - Longitude|
|MINI_THUMB_MAGIC [String]|**Mini Thumb Magic** property from Media Store|
|RESOLUTION [String]|**Resolution** of the video|
|TAGS [String]|**Tags** property from Media Store|
|ALBUM_NAME [String]|**Name of the Album** in which the video is present|

Developed By
------------

* Deepan Elango - <tizisdeepan@gmail.com>

<a href="https://twitter.com/tizisdeepan">
  <img alt="Follow me on Twitter" src="./Screenshots/twitter.png" />
</a>
<a href="https://www.linkedin.com/in/tizisdeepan/">
  <img alt="Add me to Linkedin" src="./Screenshots/linkedin.png" />
</a>
