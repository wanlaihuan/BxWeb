package com.wanbox.bxweb.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.yingt.common.util.IEventProcessor;
import com.yingt.common.util.RxThread;
import com.wanbox.bxweb.event.JsHandler;
import com.wanbox.bxweb.event.JsMessageListener;
import com.wanbox.bxweb.event.JsMessageSender;
import com.yingt.widgetkit.manager.PictureUploader;

import io.reactivex.ObservableEmitter;
import io.reactivex.Scheduler;

/**
 * @author laihuan.wan
 *         Created by laihuan.wan on 2018/01/13 0025.
 */

public class H5BoxWebView extends BridgeWebView {

    private Loader loader;
    private PictureUploader pictureUploader;

    public Loader newLoader(Fragment fragment) {
        loader = new Loader(fragment);
        return loader;
    }

    public class Loader {
        private Fragment fragment;
        private String initJsDatas;
        private CallBackFunction callBackFunction;

        public Loader(Fragment fragment) {
            this.fragment = fragment;
            pictureUploader = new PictureUploader(fragment);
            //  TODO: 构建一个 JS 数据发送器
            JsMessageSender.buildSender(H5BoxWebView.this);
            // 注册 JS 数据交互的 Handler
            RxThread.eventProcessor(new IEventProcessor() {
                @Override
                public String onSubscribe(ObservableEmitter<Object> emitter) {
                    //  TODO: 构建一个 JS 数据监听器, 因这里有处理两个循环，放置主线程阻塞，所以在子线程中处理
                    JsMessageListener.buildListener(getFragment(), H5BoxWebView.this);
                    return null;
                }

                @Override
                public Scheduler onSubscribeThreadType() {
                    return null;
                }

                @Override
                public void onAcceptUiThread(String sendId) {

                }
            });
        }

        public Fragment getFragment() {
            return fragment;
        }

        public Loader setInitJsDatas(String data) {
            setInitJsDatas(data, null);
            return this;
        }

        /**
         * @param data
         * @param callBackFunction 监听 H5 加载完毕之后 H5 的s数据返回
         * @return
         */
        public Loader setInitJsDatas(String data, CallBackFunction callBackFunction) {
            initJsDatas = data;
            this.callBackFunction = callBackFunction;
            return this;
        }

        public CallBackFunction getCallBackFunction() {
            return callBackFunction;
        }

        public String getInitJsDatas() {
            return initJsDatas;
        }

        public void loadUrl(String url) {
            H5BoxWebView.this.loadUrl(url);
        }
    }

    @Override
    public void loadUrl(String url) {
        if (loader.getFragment() != null) {
            super.loadUrl(url);
        } else {
            Log.d("H5BoxWebView", "不可直接加载，请使用加载器加载！");
        }
    }

    public H5BoxWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    public H5BoxWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWebView();
    }

    public H5BoxWebView(Context context) {
        super(context);
        initWebView();
    }

    private void initWebView() {

        setWebViewClient(new BridgeWebViewClient(this) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // TODO: 监听到页面加载结束发送信息给 H5
                if (loader != null) {
                    JsMessageSender.builder(JsHandler.HB_initH5LoadFinishedData).
                            send(loader.getInitJsDatas() == null ? "" : loader.getInitJsDatas(),
                                    loader.getCallBackFunction());
                }
            }
        });

        WebSettings webSettings = getSettings();

        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
//        webSettings.setJavaScriptEnabled(true);
//        webView.removeJavascriptInterface("searchBoxJavaBridge_");
//        intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis());
//        webView.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, intent), "sonic");

        // init webview settings
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // 处理跨域问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
    }

    /**
     * 获取头像上传处理器
     *
     * @return
     */
    public PictureUploader getPictureUploader() {
        return pictureUploader;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 处理头像上传
        pictureUploader.onResult(requestCode, resultCode, data);
    }

}
