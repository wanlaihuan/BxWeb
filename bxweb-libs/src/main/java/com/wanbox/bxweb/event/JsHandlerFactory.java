package com.wanbox.bxweb.event;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laihuan.wan
 *         Created by laihuan.wan on 2018/01/13 0022.
 *         Handler 注册工厂类，用来注册需要与 H5 数据交互的 Handler
 */

public class JsHandlerFactory {
    private String TAG = "JsHandlerFactory";
    private static JsHandlerFactory instance;
    private static Object synchronizedLock = new Object();
    /**
     * H5Box默认的 Handler
     */
    protected static List<String> commonHandlers = new ArrayList<>();
    /**
     * 开发者自定义的业务Handler
     */
    protected static List<String> customHandlers = new ArrayList<>();

    public static JsHandlerFactory getInstance() {
        if (instance == null) {
            synchronized (synchronizedLock) {
                if (instance == null) {
                    instance = new JsHandlerFactory();
                }
            }
        }
        return instance;
    }

    public JsHandlerFactory addCommonHandler(String handler) {
        commonHandlers.add(handler);
        return this;
    }

    public JsHandlerFactory addHandler(String handler) {
        if (hasCommonHandler(handler)) {
            throw new IllegalAccessError("【" + handler + "】handler 与 H5 容器中的 handler 重命名错误，请重新定义！");
        }

        customHandlers.add(handler);
        return this;
    }

    public void removeCommonHandler(String handler) {
        commonHandlers.remove(handler);
    }

    public void removeHandler(String handler) {
        customHandlers.remove(handler);
    }

    public void cleanCommonHandler() {
        commonHandlers.clear();
    }

    public void cleanHandler() {
        customHandlers.clear();
    }

    /**
     * 判断是否存在 公共 JS 接口的 handler 被注册
     *
     * @param handler
     * @return
     */
    public boolean hasCommonHandler(String handler) {

        if (commonHandlers.contains(handler)) {
            return true;
        }

        return false;
    }

    /**
     * 添加拦截器拦截 H5Box 中默认的Handler
     * @param interceptor
     * @return
     */
    public JsHandlerFactory setInterceptor(Interceptor interceptor) {

        String[] interceptHandler = interceptor.intercept();
        if (interceptHandler != null && interceptHandler.length > 0) {
            for (int i = 0; i < interceptHandler.length; i++) {
                removeCommonHandler(interceptHandler[i]);
                addHandler(interceptHandler[i]);
                Log.d(TAG, "handler = 【" + interceptHandler[i] + "】 设置了拦截器，被拦截了！");
            }
        }

        return this;
    }
}
