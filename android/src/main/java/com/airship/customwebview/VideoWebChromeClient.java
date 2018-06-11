package com.airship.customwebview;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;

import static android.view.ViewGroup.LayoutParams;

/**
 * Provides support for full-screen video on Android
 */
public class VideoWebChromeClient extends WebChromeClient {

  private final FrameLayout.LayoutParams FULLSCREEN_LAYOUT_PARAMS = new FrameLayout.LayoutParams(
      LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);

  private WebChromeClient.CustomViewCallback mCustomViewCallback;

  private Activity mActivity;
  private View mWebView;
  private View mVideoView;
  private ThemedReactContext mReactContext;
  private Boolean isVideoFullscreen;

  public VideoWebChromeClient(Activity activity, WebView webView, ThemedReactContext reactContext) {
    mWebView = webView;
    mActivity = activity;
    isVideoFullscreen = false;
    mReactContext = reactContext;
  }

  @Override
  public void onShowCustomView(View view, CustomViewCallback callback) {
    if (mVideoView != null) {
      callback.onCustomViewHidden();
      return;
    }

    WritableMap params = Arguments.createMap();
    sendEvent(mReactContext, "VideoWillEnterFullScreen", params);

    // Store the view and it's callback for later, so we can dispose of them
    // correctly
    mVideoView = view;
    mCustomViewCallback = callback;

    view.setBackgroundColor(Color.BLACK);
    view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    getRootView().addView(view, FULLSCREEN_LAYOUT_PARAMS);

    mWebView.setVisibility(View.GONE);
    isVideoFullscreen = true;
  }

  @Override
  public void onHideCustomView() {
    if (mVideoView == null) {
      return;
    }
    
    WritableMap params = Arguments.createMap();
    sendEvent(mReactContext, "VideoWillExitFullScreen", params);

    mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    mVideoView.setSystemUiVisibility(0);
    mVideoView.setVisibility(View.GONE);

    // Remove the custom view from its container.
    getRootView().removeView(mVideoView);
    mVideoView = null;
    mCustomViewCallback.onCustomViewHidden();

    mWebView.setVisibility(View.VISIBLE);
    isVideoFullscreen = false;
  }

  private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }

  private ViewGroup getRootView() {
    return ((ViewGroup) mActivity.findViewById(android.R.id.content));
  }

  public boolean onBackPressed() {
    if (isVideoFullscreen) {
      onHideCustomView();
      return true;
    } else {
      return false;
    }
  }
}
