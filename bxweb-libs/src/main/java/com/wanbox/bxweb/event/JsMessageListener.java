package com.wanbox.bxweb.event;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.yingt.uimain.base.YtBaseFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * @author laihuan.wan
 * Created by laihuan.wan on 2018/01/13 0021.
 * Handler 注册器，可注册监听 H5 的触发调用
 */

public class JsMessageListener {
    private String TAG = "JsMessageListener";

    private JsCommonIProcessor jsCommonIProcessor;
    private Fragment ytBaseFragment;
    private BridgeWebView ytBridgeWebView;

    public static void buildListener(Fragment ytBaseFragment, BridgeWebView bridgeWebView) {
        new JsMessageListener(ytBaseFragment, bridgeWebView);
    }

    public JsMessageListener(final Fragment ytBaseFragment, BridgeWebView bridgeWebView) {
        this.ytBaseFragment = ytBaseFragment;
        this.ytBridgeWebView = bridgeWebView;

        // TODO: 注册公共 JS 接口 Handler
        for (int i = 0; i < JsHandlerFactory.commonHandlers.size(); i++) {
            final String handler = JsHandlerFactory.commonHandlers.get(i);

            if (!TextUtils.isEmpty(handler)) {
                registerHandler(bridgeWebView, handler);
            }
        }

        // TODO: 注册自定义的 JS 接口 Handler
        for (int i = 0; i < JsHandlerFactory.customHandlers.size(); i++) {
            final String handler = JsHandlerFactory.customHandlers.get(i);

            if (!TextUtils.isEmpty(handler)) {
                registerHandler(bridgeWebView, handler);
            }
        }
    }

    /**
     * TODO: 注册 handler 监听 H5 的 JS 调用
     *
     * @param webView
     * @param handler
     */
    private void registerHandler(BridgeWebView webView, final String handler) {
        webView.registerHandler(handler, new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                Log.d(TAG, "handler = 【" + handler + "】 data from h5: " + data);

                // TODO: H5 JS 接口触发后构建消息
                JsMessage message = new JsMessage();
                message.setHandler(handler);
                message.setMessage(data);
                message.setOnCallbackEventBusListener(new JsMessage.OnCallbackEventBusListener() {
                    @Override
                    public void onCallback(String msg) {
                        Log.d(TAG, "handler = 【" + handler + "】 data to h5: " + msg);

                        // 回传数据给 H5
                        function.onCallBack(msg);
                    }
                });

                // TODO: 发送消息
                sendMessage(message);
            }
        });
    }

    /**
     * 发送消息
     *
     * @param message
     */
    private void sendMessage(JsMessage message) {
        if (JsHandlerFactory.getInstance().hasCommonHandler(message.handler)) {
            if (jsCommonIProcessor == null) {
                jsCommonIProcessor = new JsCommonIProcessor(ytBaseFragment, ytBridgeWebView);
            }

            jsCommonIProcessor.onJsInterfaceProcessor(message); // 调用公共的 JS 接口

        } else {
            EventBus.getDefault().post(message); // 发送该事件会触发 onJsInterfaceProcessor 方法的执行
        }
    }
}
