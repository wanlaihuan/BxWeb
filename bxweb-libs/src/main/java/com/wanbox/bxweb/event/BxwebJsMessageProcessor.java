package com.wanbox.bxweb.event;

import android.app.Activity;

import com.wanbox.bxweb.ui.BrowserFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by laihuan.wan on 2018/01/13 0028.
 */

public class BxwebJsMessageProcessor extends JsCustomIProcessor {
    private BrowserFragment browserFragment;
    private OnEventListener mOnEventListener;

    public void setOnEventListener(OnEventListener onEventListener) {
        mOnEventListener = onEventListener;
    }

    public interface OnEventListener {
        void onJsEvent(BrowserFragment browserFragment, JsMessage event);
    }

    /**
     * @param browserFragment
     */
    public void setBrowserFragment(BrowserFragment browserFragment) {
        this.browserFragment = browserFragment;
    }

    public BxwebJsMessageProcessor(Activity activity) {
        super(activity);
        EventBus.getDefault().register(BxwebJsMessageProcessor.this);
    }

    public void onDestroy() {
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

    /**
     * TODO: 发送事件总线后会触发该方法
     *
     * @param event
     */
    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJsInterfaceProcessor(JsMessage event) {
        if (mOnEventListener != null) {
            mOnEventListener.onJsEvent(browserFragment, event);
        }
    }

}
