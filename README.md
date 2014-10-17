Delectable Android
=================

This project uses the latest Android Studio from the Beta channel.

* * *
####Getting Started
* Install the latest Android Studio from the Beta channel
* Import automatic Android Code Style formatter for Java and XML

####Coding Style
* The project uses the coding style [guide] by android
* Copy AndroidStyle.xml from the codestyle folder into ~/Library/Preferences/AndroidStudioBeta/codestyles/  folder  (This comes from the android [source])
* Under AndroidStudio Prefs: Set the Code Style -> Java & XML scheme to AndroidStyle

####Credentials

There are a few credentials that are not packaged into the repository for security, they are:
* the release keystore
```
Delectable/app/release_keystore.jks
```
* the release credentials
```
Delectable/app/keystore_credentials.properties
```
* the twitter API credentials
```
Delectable/app/src/main/assets/twitter_credentials.properties
```

Please obtain these files if you need to build production releases on your machine.

[guide]:https://source.android.com/source/code-style.html
[source]:https://android.googlesource.com/platform/development/+/master/ide/intellij/codestyles/