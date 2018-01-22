/*
 * Tencent is pleased to support the open source community by making VasSonic available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 *
 */

package com.wanbox.bxweb.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.wanbox.bxweb.R;
import com.wanbox.bxweb.event.JsHandler;
import com.wanbox.bxweb.event.JsMessageListener;
import com.wanbox.bxweb.event.JsMessageSender;
import com.wanbox.bxweb.util.H5Box;
import com.yingt.common.util.IEventProcessor;
import com.yingt.common.util.RxThread;
import com.yingt.uimain.base.BaseToolbar;
import com.yingt.uimain.base.YtBaseFragment;
import com.yingt.widgetkit.manager.PictureUploader;

import io.reactivex.ObservableEmitter;
import io.reactivex.Scheduler;

//import com.tencent.sonic.sdk.SonicConfig;
//import com.tencent.sonic.sdk.SonicConstants;
//import com.tencent.sonic.sdk.SonicEngine;
//import com.tencent.sonic.sdk.SonicSession;
//import com.tencent.sonic.sdk.SonicSessionConfig;
//import com.tencent.sonic.sdk.SonicSessionConnection;

/**
 * @author laihuan.wan
 *         A demo browser activity
 *         In this demo there are three modes,
 *         sonic mode: sonic mode means webview loads html by sonic,
 *         offline mode: offline mode means webview loads html from local offline packages,
 *         default mode: default mode means webview loads html in the normal way.
 */

public class BrowserFragment extends YtBaseFragment {
    public final static String PARAM_TITLE_SHOW = "param_title_show";
    public final static String PARAM_TITLE = "param_title";
    public final static String PARAM_URL = "param_url";
    public final static String PARAM_MODE = "param_mode";
    public final static String PARAM_LOAD_FINISHED_DATAS = "view_load_finished_datas";
    public final static String PARAM_IS_SET_TITLE_BAR_COLOR = "is_set_title_bar_color";
    public final static String PARAM_STATUS_BAR_SHOW = "status_bar_show";
    public final static String PARAM_STATUS_BAR_COLOR = "status_bar_color";

    private Intent intent;
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_SONIC = 1;
    public static final int MODE_SONIC_WITH_OFFLINE_CACHE = 2;
    public static final int RESULT_CODE = 0;

    private String mSetTitle;
    ValueCallback<Uri> mUploadMessage;

    private BridgeWebView webView;
//    private SonicSession sonicSession;

    private boolean isShowTitle = true;
    //默认不支持沉浸模式
    private boolean isTranslucentBars = false;

    private String viewLoadFinishedDatas = "{\"ok\"}";

    private PictureUploader pictureUploader;

    /**
     * 设置是否显示 title
     *
     * @param isShowTitle
     */
    public void setTitleVisibility(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    public void setTranslucentBars(boolean translucentBars) {
        this.isTranslucentBars = translucentBars;
    }

    @Override
    public boolean isRemovedToolbar() {
        return !isShowTitle;
    }

    @Override
    public boolean isTranslucentBars() {
        return isTranslucentBars;
    }

    @Override
    public BaseToolbar getLayoutToolbarView() {
        return super.getLayoutToolbarView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.box_web_browser_layout;
    }

    @Override
    public void onFindViewById() {
        super.onFindViewById();

        intent = getActivity().getIntent();
        Bundle bundle = H5Box.with().getBundle();//intent.getExtras();
        mSetTitle = bundle.getString(PARAM_TITLE);
        String url = bundle.getString(PARAM_URL);
        int mode = bundle.getInt(PARAM_MODE, 1);
        viewLoadFinishedDatas =
                bundle.getString(PARAM_LOAD_FINISHED_DATAS, viewLoadFinishedDatas);
        if (TextUtils.isEmpty(url) || -1 == mode) {
            getActivity().finish();
            return;
        }

        pictureUploader = new PictureUploader(this);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        if (!TextUtils.isEmpty(mSetTitle)) {
            setToolbarTitle(mSetTitle);
        }

//        // init sonic engine if necessary, or maybe u can do this when application created
//        if (!SonicEngine.isGetInstanceAllowed()) {
//            SonicEngine.createInstance(new SonicRuntimeImpl(getContext()), new SonicConfig.Builder().build());
//        }
//
//        SonicSessionClientImpl sonicSessionClient = null;
//
//        // if it's sonic mode , startup sonic session at first time
//        sonicSession = SonicEngine.getInstance().createSession(url, new SonicSessionConfig.Builder().build());
//        if (null != sonicSession) {
//            sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
//        } else {
//            // this only happen when a same sonic session is already running,
//            // u can comment following codes to feedback as a default mode.
//            throw new UnknownError("create session fail!");
//        }

        // init webview
        webView = (BridgeWebView) findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
                chooserIntent.setType("image/*");
                startActivityForResult(chooserIntent, RESULT_CODE);
            }
        });

        webView.setWebViewClient(new BridgeWebViewClient(webView) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                String title = view.getTitle();
                if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(mSetTitle)) {
                    setToolbarTitle(title); // 设置 title
                }

//                if (sonicSession != null) {
//                    sonicSession.getSessionClient().pageFinish(url);
//                }

                // TODO: 监听到页面加载结束发送信息给 H5
                JsMessageSender.builder(JsHandler.HB_initH5LoadFinishedData).
                        send(viewLoadFinishedDatas, H5Box.with().getCallBackFunction());
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                if (sonicSession != null) {
//                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
//                }
                return null;
            }
        });

        //  TODO: 构建一个 JS 数据发送器
        JsMessageSender.buildSender(webView);
        // 注册 JS 数据交互的 Handler
        RxThread.eventProcessor(new IEventProcessor() {
            @Override
            public String onSubscribe(ObservableEmitter<Object> emitter) {
                //  TODO: 构建一个 JS 数据监听器, 因这里有处理两个循环，放置主线程阻塞，所以在子线程中处理
                JsMessageListener.buildListener(BrowserFragment.this, webView);
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

        WebSettings webSettings = webView.getSettings();

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

        // webview is ready now, just tell session client to bind
//        if (sonicSessionClient != null) {
//            sonicSessionClient.bindWebView(webView);
//            sonicSessionClient.clientReady();
//        } else { // default mode
        webView.loadUrl(url);
//        }
    }

/*
    private static class OfflinePkgSessionConnection extends SonicSessionConnection {

        private final WeakReference<Context> context;

        public OfflinePkgSessionConnection(Context context, SonicSession session, Intent intent) {
            super(session, intent);
            this.context = new WeakReference<Context>(context);
        }

        @Override
        protected int internalConnect() {
            Context ctx = context.get();
            if (null != ctx) {
                try {
                    InputStream offlineHtmlInputStream = ctx.getAssets().open("sonic-demo-index.html");
                    responseStream = new BufferedInputStream(offlineHtmlInputStream);
                    return SonicConstants.ERROR_CODE_SUCCESS;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return SonicConstants.ERROR_CODE_UNKNOWN;
        }

        @Override
        protected BufferedInputStream internalGetResponseStream() {
            return responseStream;
        }

        @Override
        public void disconnect() {
            if (null != responseStream) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getResponseCode() {
            return 200;
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return new HashMap<>(0);
        }

        @Override
        public String getResponseHeaderField(String key) {
            return "";
        }
    }*/

    /**
     * 获取头像上传处理器
     *
     * @return
     */
    public PictureUploader getPictureUploader() {
        return pictureUploader;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BrowserFragment.RESULT_CODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else {
            // 处理头像上传
            pictureUploader.onResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
//        if (null != sonicSession) {
//            sonicSession.destroy();
//            sonicSession = null;
//        }
        super.onDestroy();
    }

    /**
     * 监听返回事件
     *
     * @return 是否继续往下传递
     */
    @Override
    public boolean onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return false;
        }
        return true;
    }
}
