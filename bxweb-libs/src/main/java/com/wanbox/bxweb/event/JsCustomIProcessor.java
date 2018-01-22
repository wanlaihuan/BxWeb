package com.wanbox.bxweb.event;

import android.app.Activity;

import com.yingt.common.util.IEventProcessor;
import com.yingt.common.util.RxThread;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import io.reactivex.ObservableEmitter;
import io.reactivex.Scheduler;

/**
 * @author laihuan.wan
 *         Created by laihuan.wan on 2018/01/13 0022.
 */

public abstract class JsCustomIProcessor implements Serializable{

    private Activity activity;

    public JsCustomIProcessor(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void register() {
        EventBus.getDefault().register(JsCustomIProcessor.this);
//        RxThread.eventProcessor(new IEventProcessor() {
//            @Override
//            public String onSubscribe(ObservableEmitter<Object> emitter) {
//                if (!EventBus.getDefault().isRegistered(JsCustomIProcessor.this)) {
//                    EventBus.getDefault().register(JsCustomIProcessor.this);
//                }
//                return null;
//            }
//
//            @Override
//            public Scheduler onSubscribeThreadType() {
//                return null;
//            }
//
//            @Override
//            public void onAcceptUiThread(String sendId) {
//
//            }
//        });
    }

    public void unregister() {
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

    /**
     * TODO: 发送事件总线后会触发该方法
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public abstract void onJsInterfaceProcessor(JsMessage event);
}
