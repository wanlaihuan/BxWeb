package com.wanbox.bxweb.util;

import android.app.Activity;

import com.wanbox.bxweb.event.Interceptor;
import com.wanbox.bxweb.event.JsCustomIProcessor;
import com.wanbox.bxweb.event.JsHandlerFactory;
import com.wanbox.bxweb.event.JsMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by laihuan.wan on 2018/01/13 0008.
 */

public class H5BoxJs extends JsCustomIProcessor {
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
        H5BoxJs h5BoxJs;

        public Builder(Activity activity) {
            this.activity = activity;
            h5BoxJs = new H5BoxJs(activity);
            h5BoxJs.register();
        }

        public Builder register() {
            h5BoxJs.register();
            return this;
        }

        public Builder onDestroy() {
            h5BoxJs.unregister();
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
         * 设置拦截器
         *
         * @param interceptor
         * @return
         */
        public Builder setInterceptor(Interceptor interceptor) {
            JsHandlerFactory.getInstance().setInterceptor(interceptor);
            return this;
        }

        /**
         * 设置 JS 事件监听
         *
         * @param onEventListener
         * @return
         */
        @Deprecated
        public Builder setOnEventListener(OnEventListener onEventListener) {
            h5BoxJs.setOnEventListener(onEventListener);
            return this;
        }

        /**
         * 设置 JS 事件监听
         *
         * @param onEventListener
         * @return
         */
        public Builder setOnJsEventListener(OnEventListener onEventListener) {
            h5BoxJs.setOnEventListener(onEventListener);
            return this;
        }
    }

    public H5BoxJs(Activity activity) {
        super(activity);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJsInterfaceProcessor(JsMessage event) {
        if (mOnEventListener != null)
            mOnEventListener.onJsEvent(event);
    }

    private OnEventListener mOnEventListener;

    public void setOnEventListener(OnEventListener onEventListener) {
        mOnEventListener = onEventListener;
    }

    public interface OnEventListener {
        void onJsEvent(JsMessage event);
    }
}
