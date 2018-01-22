package com.wanbox.bxweb.event;

/**
 * @author laihuan.wan
 * Created by laihuan.wan on 2018/01/13 0008.
 */

public class JsHandler {
    // --------------------  以下是原生主动调用 H5 传递数据的 Handler  -----------------------
    // TODO: 动态更新定位数据
    public static final String HB_updateLocationData = "updateLocationData";
    // TODO: 网页加载完成,传递给 H5 的初始数据
    public static final String HB_initH5LoadFinishedData = "initH5LoadFinishedData";



    // --------------------  以下是 H5 主动调用原生的传递数据 Handler  ------------------------
    // TODO: 打开原生界面加载 H5
    public static final String HB_openNativeWindow = "openNativeWindow";
    // TODO: 关闭当前的窗口页面
    public static final String HB_closeNativeWindow = "closeNativeWindow";
    // TODO: 原生数据存储
    public static final String HB_setKeyValue = "setKeyValue";
    // TODO: 原生数据获取
    public static final String HB_getKeyValue = "getKeyValue";
    // TODO: 启动定位功能
    public static final String HB_startLoactionService = "startLoactionService";
    // TODO: 关闭定位功能
    public static final String HB_stopLoactionService = "stopLoactionService";
    // TODO: 调用键盘宝2.0键盘输入
    public static final String HB_showKeyboard = "showKeyboard";
    // TODO: 原生 Toast 信息提示
    public static final String HB_showToast = "showToast";
    // TODO: 原生图片预览
    public static final String HB_showPhotoPreview = "showPhotoPreview";
    // TODO: 视频播放
    public static final String HB_showVideoPlay = "showVideoPlay";
    // TODO: 显示对话框
    public static final String HB_showDialog = "showDialog";
    // TODO: 分享
    public static final String HB_showShareDialog = "showShareDialog";
    // TODO: 日期选择器
    public static final String HB_showDatePicker = "showDatePicker";
    // TODO: 调用相册和摄像头
    public static final String HB_showCameraAndPhoto = "pictureUpload";
}
