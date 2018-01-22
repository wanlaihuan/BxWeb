package com.wanbox.bxweb.util;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.wanbox.bxweb.event.BxwebJsMessageProcessor;
import com.wanbox.bxweb.event.Interceptor;
import com.wanbox.bxweb.event.JsHandlerFactory;
import com.wanbox.bxweb.ui.BrowserFragment;
import com.wanbox.bxweb.ui.YtBrowserActivity;
import com.yingt.uimain.util.UiMain;

/**
 * @author laihuan.wan
 *         Created by laihuan.wan on 2018/01/13 0021.
 */

public class H5Box {

    private static Builder mBuilder;

    public static Builder with(Activity activity) {
        mBuilder = new Builder(activity);
        return mBuilder;
    }

    public static Builder with() {
        return mBuilder;
    }

    public static class Builder {
        private Activity activity;
        private Bundle bundle; // 数据传递
        private String title;
        private String initJsDatas;
        private boolean isSetToolbarColor;
        private boolean isShowTitle;
        private boolean isTranslucentBars;//默认值就是个false了
        private int toolbarColor;
        private CallBackFunction callBackFunction;
        private BxwebJsMessageProcessor bxwebJsMessageProcessor;

        public Builder(Activity activity) {
            this.activity = activity;
            isShowTitle = false;
            isSetToolbarColor = false;
            isTranslucentBars = false;
            callBackFunction = null;
            bundle = null;
        }

        public Builder setBundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public Bundle getBundle() {
            return bundle;
        }

        public Builder setTitleText(String text) {
            title = text;
            return this;
        }

        public Builder setToolbarBackgroundColor(int toolbarColor) {
            isSetToolbarColor = true;
            this.toolbarColor = toolbarColor;
            return this;
        }

        public Builder setTitleShow(boolean isShow) {
            isShowTitle = isShow;
            return this;
        }

        public Builder setIsTranslucentBars(boolean isTranslucentBars) {
            this.isTranslucentBars = isTranslucentBars;
            return this;
        }

        public Builder setInitJsDatas(String data) {
            setInitJsDatas(data, null);
            return this;
        }

        /**
         * @param data
         * @param callBackFunction 监听 H5 加载完毕之后 H5 的s数据返回
         * @return
         */
        public Builder setInitJsDatas(String data, CallBackFunction callBackFunction) {
            initJsDatas = data;
            this.callBackFunction = callBackFunction;
            return this;
        }

        /**
         * 设置拦截器, 拦截公共的 handler
         *
         * @param interceptor
         * @return
         */
        public Builder setInterceptor(Interceptor interceptor) {
            JsHandlerFactory.getInstance().setInterceptor(interceptor);
            return this;
        }

        /**
         * 添加 js 交互 handler
         *
         * @param handlers
         * @return
         */
        public Builder addHandler(String[] handlers) {
            if (handlers != null) {
                for (int i = 0; i < handlers.length; i++) {
                    // 添加自定义的与 H5 约定好的交互 handler
                    JsHandlerFactory.getInstance().addHandler(handlers[i]);
                }
            }
            return this;
        }

        /**
         * 设置 JS 事件监听
         *
         * @param onEventListener
         * @return
         */
        public Builder setOnJsEventListener(BxwebJsMessageProcessor.OnEventListener onEventListener) {
            bxwebJsMessageProcessor = new BxwebJsMessageProcessor(activity);
            bxwebJsMessageProcessor.setOnEventListener(onEventListener);
            return this;
        }

        public CallBackFunction getCallBackFunction() {
            return callBackFunction;
        }

        public void go(String htmlUrl) {
            Bundle bundle = new Bundle();
            bundle.putString(BrowserFragment.PARAM_TITLE, title);
            bundle.putBoolean(BrowserFragment.PARAM_TITLE_SHOW, isShowTitle);
            bundle.putString(BrowserFragment.PARAM_URL, htmlUrl);
            bundle.putBoolean(BrowserFragment.PARAM_STATUS_BAR_SHOW, isTranslucentBars);
            bundle.putBoolean(BrowserFragment.PARAM_IS_SET_TITLE_BAR_COLOR, isSetToolbarColor);
            if (!TextUtils.isEmpty(initJsDatas)) {
                bundle.putString(BrowserFragment.PARAM_LOAD_FINISHED_DATAS, initJsDatas);
            }
            if (isSetToolbarColor) {
                bundle.putInt(BrowserFragment.PARAM_STATUS_BAR_COLOR, toolbarColor);
            }

            setBundle(bundle);// 绑定数据
            UiMain.bindingTempDatas(bxwebJsMessageProcessor);
            UiMain.with(activity)
//                    .bundingdata(bundle)
                    .go(YtBrowserActivity.class);
        }
    }

}
