package com.wanbox.bxweb;

import com.wanbox.bxweb.event.JsHandler;
import com.wanbox.bxweb.event.JsHandlerFactory;

/**
 * @author laihuan.wan
 *         Created by laihuan.wan on 2018/01/13 0008.
 */

public class H5BoxInit {

    public static void init() {
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
