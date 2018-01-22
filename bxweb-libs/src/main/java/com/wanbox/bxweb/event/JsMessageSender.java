package com.wanbox.bxweb.event;

import android.text.TextUtils;
import android.util.Log;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

/**
 * @author laihuan.wan
 * Created by laihuan.wan on 2018/01/13 0007.
 */

public class JsMessageSender {
    private static final String TAG = "JsMessageSender";

    private static Builder builder;

    public static Builder buildSender(BridgeWebView bridgeWebView) {
        if (builder == null) {
            builder = new Builder(bridgeWebView);
            builder.setHandler(null);
        }

        return builder;
    }

    public static Builder builder(String handler) {
        builder.setHandler(handler);
        return builder;
    }

    public static class Builder {
        private BridgeWebView bridgeWebView;
        // 数据交互的 handelr
        private String mHandler = null;

        public Builder(BridgeWebView bridgeWebView) {
            this.bridgeWebView = bridgeWebView;
        }

        public void setHandler(String handler){
            mHandler = handler;
        }
        /**
         * 直接发送字符串数据给 H5 并可监听 H5 的数据响应
         * @param sendData 发送的数据
         * @param callback H5 的数据回调
         * @return
         */
        public Builder send(String sendData, CallBackFunction callback){
            // TODO: 发送定位信息给 H5
            JsMessage message = new JsMessage();
            message.setMessage(sendData);
            send(message, callback);
            return this;
        }

        /**
         * TODO: 原生给 H5 发送消息接口
         * @param jsMessage 发送给 H5 的消息数据
         * @param callBackFunction 监听 H5 的数据回调
         * @return
         */
        public Builder send(final JsMessage jsMessage, final CallBackFunction callBackFunction){
            if(TextUtils.isEmpty(jsMessage.getHandler())){
                jsMessage.setHandler(mHandler);
            }
            jsMessage.setOnCallbackEventBusListener(new JsMessage.OnCallbackEventBusListener() {
                @Override
                public void onCallback(String msg) {
                    // TODO: 正在给 H5 发送消息
                    Log.d(TAG, "handler = 【" + jsMessage.handler + "】 data to h5: " + msg);
                    if(callBackFunction == null){
                        CallBackFunction callBackFunction1 = new CallBackFunction(){
                            @Override
                            public void onCallBack(String data) {
                                Log.d(TAG, "handler = 【" + jsMessage.handler + "】 data from h5: " + data);
                            }
                        };
                        bridgeWebView.callHandler(jsMessage.handler, msg, callBackFunction1);
                    }else {
                        bridgeWebView.callHandler(jsMessage.handler, msg, callBackFunction);
                    }
                }
            });
            // TODO: 开始给 H5 发送消息
            jsMessage.onCallback(jsMessage.getMessage());
            return this;
        }
    }

}
