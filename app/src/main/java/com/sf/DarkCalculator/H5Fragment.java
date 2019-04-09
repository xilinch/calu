package com.sf.DarkCalculator;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class H5Fragment extends Fragment {

    private View view;

    private LinearLayout ll_content;

    private RelativeLayout rl_header;

    private LinearLayout ll_back;

    private NfWebView nfWebView;

    private boolean canback;

//    private ADTextBean adTextBean;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_h5, container, false);
            ll_content = view.findViewById(R.id.ll_content);
            rl_header = view.findViewById(R.id.rl_header);
            ll_back = view.findViewById(R.id.ll_back);
            nfWebView = new NfWebView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            ll_content.addView(nfWebView,layoutParams);
            url = getArguments().getString("url", "");
            canback = getArguments().getBoolean("canback");
            if(canback){
                rl_header.setVisibility(View.GONE);
            }
            initWebview();

            nfWebView.loadUrl(url);
        }
        return view;
    }


    private void initWebview(){
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canback){
                    if(nfWebView.canGoBack()){
                        nfWebView.goBack();
                    }
                } else {
                    getActivity().finish();
                }
            }
        });

        nfWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                LogUtil.e("my","onProgressChanged:" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LogUtil.e("my","onReceivedTitle:" + title);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                showCustomView(view,callback);
                LogUtil.e("my", "onShowCustomView ");
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                LogUtil.e("my", "onHideCustomView ");
                hideCustomView();
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                LogUtil.e("my", "onCreateWindow ");
                if (nfWebView == null) {
                    nfWebView = new NfWebView(getActivity());
                }
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(nfWebView);
                resultMsg.sendToTarget();
                return true;
            }
        });

        nfWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("my","shouldOverrideUrlLoading:" + url);
                if(URLUtil.isValidUrl(url)){
                    view.loadUrl(url);

                }
                return true;
            }

        });
    }

    /**
     * 返回
     */
    public void goback(){
        if(canback){
            if(nfWebView.canGoBack()){
                nfWebView.goBack();
            }
        } else {
            getActivity().finish();
        }
    }


    /** 视频全屏参数 */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    /** 视频播放全屏 **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        getActivity().getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(getActivity());
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /** 隐藏视频全屏 */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        nfWebView.setVisibility(View.VISIBLE);
    }

    /** 全屏容器界面 */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getActivity().getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public boolean onkeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
                if (customView != null) {
                    hideCustomView();
                } else if (nfWebView.canGoBack()) {
                    nfWebView.goBack();
                } else {
                    getActivity().finish();
                }
                return true;
            default:
                return false;
        }
    }

}
