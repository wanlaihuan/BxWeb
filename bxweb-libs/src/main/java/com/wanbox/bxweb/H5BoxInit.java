package com.wanbox.bxweb;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.wanbox.bxweb.event.JsHandler;
import com.wanbox.bxweb.event.JsHandlerFactory;
import com.yingt.uimain.util.Res;

/**
 * @author laihuan.wan
 *         Created by laihuan.wan on 2018/01/13 0008.
 */

public class H5BoxInit {

    /**
     *
     * @param application
     */
    public static void init(Application application) {
        // TODO: 解决 7.0 以上的手机启动摄像头闪退的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        // 初始化获取资源类
        Res.setContext(application);
        registerCommonHandler();
    }

    /**
     * 注册公共接口的 handler
     */
    private static void registerCommonHandler() {
        // TODO: 注册默认的公共 JS 接口 handler
        JsHandlerFactory.getInstance()
                // TODO: open/close 类型接口
                .addCommonHandler(JsHandler.HB_openNativeWindow) // TODO: 打开原生窗口页面
                .addCommonHandler(JsHandler.HB_closeNativeWindow) // TODO: 关闭当前的窗口页面

                // TODO: get/set 类型接口
                .addCommonHandler(JsHandler.HB_setKeyValue) // TODO: 数据存储
                .addCommonHandler(JsHandler.HB_getKeyValue) // TODO: 数据获取

                // TODO: start/stop 类型接口
                .addCommonHandler(JsHandler.HB_startLoactionService) // TODO: 开启定位服务
                .addCommonHandler(JsHandler.HB_stopLoactionService) // TODO: 关闭定位服务

                // TODO: show 类型接口
                .addCommonHandler(JsHandler.HB_showKeyboard) // TODO: 调用键盘宝2.0键盘输入
                .addCommonHandler(JsHandler.HB_showToast) //  TODO: Tost 提示
                .addCommonHandler(JsHandler.HB_showPhotoPreview) // TODO: 图片预览
                .addCommonHandler(JsHandler.HB_showVideoPlay) // TODO: 原生视频
                .addCommonHandler(JsHandler.HB_showDialog) // TODO: App 更新对话框
                .addCommonHandler(JsHandler.HB_showShareDialog) // TODO: 分享对话框
                .addCommonHandler(JsHandler.HB_showDatePicker) // TODO: 日期选择器
                .addCommonHandler(JsHandler.HB_showCameraAndPhoto)// TODO: 调用相册和摄像头
        ;
    }
}
