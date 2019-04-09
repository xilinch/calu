package com.sf.DarkCalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class NfWebView extends WebView {

	public NfWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWebViewSettings();
	}
	public NfWebView(Context context) {
		super(context);
		initWebViewSettings();
	}

	public NfWebView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		initWebViewSettings();

	}

	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setPluginState(WebSettings.PluginState.ON);
		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

//		webSetting.setSupportMultipleWindows(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		//增加
		webSetting.setTextSize(WebSettings.TextSize.NORMAL);
		//自动播放
		webSetting.setMediaPlaybackRequiresUserGesture(true);
		webSetting.setGeolocationDatabasePath(getContext().getFilesDir().getPath());
		// 设置可以支持缩放
		webSetting.setSupportZoom(true);
		//支持混合模式
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		//接口禁止(直接或反射)调用，避免视频画面无法显示：
//		setLayerType(View.LAYER_TYPE_SOFTWARE,null);
		setDrawingCacheEnabled(true);
		// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计
		CookieSyncManager.createInstance(getContext());
		CookieSyncManager.getInstance().sync();
	}



}
