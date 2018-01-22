package com.wanbox.bxweb.event;

import java.io.Serializable;

/**
 * @author laihuan.wan
 *         Created by laihuan.wan on 2018/01/13 0021.
 *         JS 交互接口的事件模型
 */

public class JsMessage implements Serializable {
    public String handler = "";
    private String message;
    private OnCallbackEventBusListener onCallbackEventBusListener;

    public interface OnCallbackEventBusListener {
        /**
         * 消息回调
         *
         * @param msg
         */
        void onCallback(String msg);
    }

    public void onCallback(String msg) {
        if (onCallbackEventBusListener != null) {
            onCallbackEventBusListener.onCallback(msg);
        }
    }

    public JsMessage() {

    }

    /**
     * TODO: 设置回调监听器，监听 EventBus 的响应数据
     *
     * @param onCallbackEventbusListener
     */
    public void setOnCallbackEventBusListener(OnCallbackEventBusListener onCallbackEventbusListener) {
        this.onCallbackEventBusListener = onCallbackEventbusListener;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getHandler() {
        return handler;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
