package com.wanbox.bxweb.event;

/**
 * @author laihuan.wan
 * Created by laihuan.wan on 2018/01/13 0008.
 */

public interface Interceptor {

    /**
     * Handler 事件拦截器
     * @return 设置需要拦截的 Handler 的集合
     */
    String[] intercept();
}
