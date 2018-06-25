## react-native-android-fullscreen-webview

## Overview

The WebView component that comes with React Native doesn't have the ability to show `s
fullscreen by default. This package essentially copies the existing WebView for Android and adds some extra functionality to enable fullscreen videos. This behavior is supported by default for iOS, so this will simply use the native iOS WebView.

## Getting started

`yarn add hummingbird-me/react-native-android-fullscreen-webview`


##### Automatically link the library

`react-native link`

##### Update AndroidManifest.xml

Under your main activity, make sure you add:

```
android:configChanges="orientation"
android:hardwareAccelerated="true"
```

Example:

```
<activity
  android:name=".MainActivity"
  android:label="@string/app_name"
  android:launchMode="singleTop"
  android:screenOrientation="portrait"
  android:configChanges="keyboard|keyboardHidden|screenSize|orientation"
  android:windowSoftInputMode="adjustPan"
  android:hardwareAccelerated="true"
>
```

## Usage

This works exactly like the existing `WebView` component, so there is nothing extra that needs to be added.

```
import WebView from 'react-native-android-fullscreen-webview';

...

<WebView
  source={{ uri: 'https://www.youtube.com/embed/dQw4w9WgXcQ' }}
/>
```

---

MIT License.

---
