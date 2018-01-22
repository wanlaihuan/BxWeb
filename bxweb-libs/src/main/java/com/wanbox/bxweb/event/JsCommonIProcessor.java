package com.wanbox.bxweb.event;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.google.gson.Gson;
import com.yingt.common.util.DateUtils;
import com.yingt.common.util.DensityUtils;
import com.yingt.common.util.StringUtils;
import com.yingt.datepicker.DateScrollerDialog;
import com.yingt.datepicker.data.Type;
import com.yingt.datepicker.listener.OnDateSetListener;
import com.wanbox.bxweb.model.BaseInfo;
import com.wanbox.bxweb.model.KeyValueInfo;
import com.wanbox.bxweb.model.LocationInfo;
import com.wanbox.bxweb.model.OpenWinInfo;
import com.wanbox.bxweb.model.PhotoPreInfo;
import com.wanbox.bxweb.model.VideoInfo;
import com.wanbox.bxweb.ui.BrowserFragment;
import com.wanbox.bxweb.util.H5Box;
import com.wanbox.bxweb.view.H5BoxWebView;
import com.yingt.uimain.base.YtBaseFragment;
import com.yingt.uimain.location.OnLocationListener;
import com.yingt.uimain.location.YtLocationMgr;
import com.yingt.uimain.ui.PhotoActivity;
import com.yingt.uimain.ui.VideoPlayFragment;
import com.yingt.uimain.util.UiMain;
import com.yingt.widgetkit.dialog.YtShareDialog;
import com.yingt.widgetkit.dialog.YtUpdateDialog;
import com.yingt.widgetkit.manager.PictureUploader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author laihuan.wan
 *         Created by laihuan.wan on 2018/01/13 0024.
 */

public class JsCommonIProcessor {

    private final String PREFERENCE_NAME = "H5Box";
    private final String ERROR = "{\"result\":\"error\"}";
    private final String OK = "{\"result\":\"ok\"}";

    private Fragment ytBaseFragment;
    private BridgeWebView ytBridgeWebView;
    int count = 0;

    public JsCommonIProcessor(Fragment fragment, BridgeWebView bridgeWebView) {
        this.ytBaseFragment = fragment;
        ytBridgeWebView = bridgeWebView;
    }

    /**
     * H5Box 固有封装的公共 JS 接口监听
     *
     * @param event 消息事件
     */
    public void onJsInterfaceProcessor(final JsMessage event) {

        if (event.handler.equals(JsHandler.HB_showCameraAndPhoto)) {
            // TODO: 支持调用相册和摄像头拍照功能
            if (ytBaseFragment instanceof BrowserFragment) {
                // 支持在新的 H5 页面中
                BrowserFragment browserFragment = (BrowserFragment) ytBaseFragment;
                browserFragment.getPictureUploader().choose();
                browserFragment.getPictureUploader().setOnCallbackEventListener(
                        new PictureUploader.OnCallBase64Listener() {
                            @Override
                            public void onCallback(String base64Str, Bitmap bitmap) {
                                event.onCallback("{\"base64Str\":\"" + base64Str + "\"}");
                            }
                        });
            } else if (ytBridgeWebView instanceof H5BoxWebView) {
                // 支持界面加载 局部 webview 的情况
                // 在对应的 onActivityResult 方法中一定要调用 webView 的 onActivityResult
                H5BoxWebView h5BoxWebView = (H5BoxWebView) ytBridgeWebView;
                h5BoxWebView.getPictureUploader().choose();
                h5BoxWebView.getPictureUploader().setOnCallbackEventListener(
                        new PictureUploader.OnCallBase64Listener() {
                            @Override
                            public void onCallback(String base64Str, Bitmap bitmap) {
                                event.onCallback("{\"base64Str\":\"" + base64Str + "\"}");
                            }
                        });
            }
        } else if (event.handler.equals(JsHandler.HB_showKeyboard)) { // TODO: 调用键盘宝2.0键盘输入
            if (ytBaseFragment instanceof YtBaseFragment) {
                ((YtBaseFragment) ytBaseFragment).getKeyboardManager()
                        .loadKeyboardAndShow(-1, 300, null, null);
                event.onCallback(OK);
            }

        } else if (event.handler.equals(JsHandler.HB_closeNativeWindow)) { // TODO: 关闭当前的窗口页面
            ytBaseFragment.getActivity().finish();
            event.onCallback(OK);

        } else if (event.handler.equals(JsHandler.HB_openNativeWindow)) { // TODO: 打开原生界面加载 H5
            Gson gson = new Gson();
            OpenWinInfo info = gson.fromJson(event.getMessage(), OpenWinInfo.class);
            if (!TextUtils.isEmpty(info.getUrl())) {
                H5Box.with(ytBaseFragment.getActivity())
                        .setTitleShow(info.isShowNativeTitle())
                        .go(info.getUrl());
            }
            event.onCallback(OK);

        } else if (event.handler.equals(JsHandler.HB_setKeyValue)) { // TODO: 原生数据存储
            String msg = event.getMessage();
            try {
                Gson gson = new Gson();
                KeyValueInfo info = gson.fromJson(msg, KeyValueInfo.class);

                if (!TextUtils.isEmpty(info.getDataType())) {
                    SharedPreferences settings = ytBaseFragment.getContext().
                            getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(info.getDataType(), info.getDataValue());
                    editor.commit();
                } else {
                    event.onCallback(ERROR);
                }

            } catch (Exception e) {
                event.onCallback(ERROR);
            }
            event.onCallback(OK);

        } else if (event.handler.equals(JsHandler.HB_getKeyValue)) { // TODO: 原生数据获取
            try {
                Gson gson = new Gson();
                KeyValueInfo info = gson.fromJson(event.getMessage(), KeyValueInfo.class);

                if (!TextUtils.isEmpty(info.getDataType())) {
                    SharedPreferences settings = ytBaseFragment.getContext().
                            getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
                    String value = settings.getString(info.getDataType(), "");
                    event.onCallback(value); // 数据回调给 H5

                } else {
                    event.onCallback(ERROR);
                }
            } catch (Exception e) {
                event.onCallback(ERROR);
            }
            event.onCallback(OK);

        } else if (event.handler.equals(JsHandler.HB_showToast)) { // TODO: 原生 Toast 信息提示
            Gson gson = new Gson();
            BaseInfo info = gson.fromJson(event.getMessage(), BaseInfo.class);
            Toast.makeText(ytBaseFragment.getContext(), info.getDefData(), Toast.LENGTH_SHORT).show();
            event.onCallback(OK);

        } else if (event.handler.equals(JsHandler.HB_showPhotoPreview)) { // TODO: 原生图片预览
            Gson gson = new Gson();
            PhotoPreInfo info = gson.fromJson(event.getMessage(), PhotoPreInfo.class);
            int position = StringUtils.stringToInt(info.getPosition());

            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            List<PhotoPreInfo.ImageUrlsBean> imageUrls = info.getImageUrls();
            if (imageUrls != null) {
                ArrayList<String> images = new ArrayList<>();
                for (int i = 0; i < imageUrls.size(); i++) {
                    images.add(imageUrls.get(i).getImageUrl());
                }
                bundle.putStringArrayList("imageUrls", images);
            }

            // 启动相册 Fragment 的页面
            UiMain.with(ytBaseFragment.getActivity())
                    .bundingdata(bundle)
                    .go(PhotoActivity.class);

            event.onCallback(OK);

        } else if (event.handler.equals(JsHandler.HB_showVideoPlay)) { // TODO: 视频播放
            Gson gson = new Gson();
            VideoInfo info = gson.fromJson(event.getMessage(), VideoInfo.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", info.getTitle());
            bundle.putString("width", info.getWidth());
            bundle.putString("yRatio", info.getYRatio());
            bundle.putString("image", info.getImage());
            bundle.putString("src", info.getSrc());

            UiMain.with(ytBaseFragment.getActivity())
                    .bundingdata(bundle)
                    .goFragment(VideoPlayFragment.class);

            event.onCallback(OK);

        } else if (event.handler.equals(JsHandler.HB_showDialog)) { // TODO: 显示对话框
            YtUpdateDialog.with()
                    .setHeight((int) (DensityUtils.getScreenMin() * 0.62f))
                    .setOutCancel(false)
                    .show(ytBaseFragment.getActivity().getSupportFragmentManager());
            event.onCallback(OK);

        } else if (event.handler.equals(JsHandler.HB_showShareDialog)) { // TODO: 分享
            YtShareDialog.with()
                    .setShowBottom(true)
                    .show(ytBaseFragment.getActivity().getSupportFragmentManager());
            event.onCallback(OK);

        } else if (event.handler.equals(JsHandler.HB_showDatePicker)) { // TODO: 日期选择器
            DateScrollerDialog dialog = new DateScrollerDialog.Builder()
                    .setType(Type.YEAR_MONTH_DAY)
                    .setTitleStringId("")
                    .setCurMilliseconds(System.currentTimeMillis())
                    .setCallback(new OnDateSetListener() {
                        @Override
                        public void onDateSet(DateScrollerDialog timePickerView, long milliseconds) {
                            String date = DateUtils.format_YYYY_MM_DD(new Date(milliseconds));
                            event.onCallback(date);
                        }
                    })
                    .build();
            if (dialog != null && !dialog.isAdded()) {
                dialog.show(ytBaseFragment.getActivity().getSupportFragmentManager(), "year_month_day");
                event.onCallback(OK);
            } else {
                event.onCallback(ERROR);
            }

        } else if (event.handler.equals(JsHandler.HB_startLoactionService)) {  // TODO: 启动定位功能
            YtLocationMgr.with(ytBaseFragment.getContext()).startLocation(new OnLocationListener() {
                @Override
                public void updateLastLocation(Location location) {

                    // TODO: 最近一次的数据直接回调给 web
                    LocationInfo info = new LocationInfo();
                    info.setLongitude(location.getLongitude());
                    info.setLatitude(location.getLatitude());
                    info.setServiceFlag(true);
                    Gson gson = new Gson();
                    String msgContent = gson.toJson(info);

                    event.onCallback((count++) + msgContent);
                }

                @Override
                public void updateLocation(Location location) {
                    LocationInfo info = new LocationInfo();
                    info.setLongitude(location.getLongitude());
                    info.setLatitude(location.getLatitude());
                    info.setServiceFlag(true);
                    Gson gson = new Gson();
                    String msgContent = gson.toJson(info);

                    // TODO: 发送定位信息给 H5
                    JsMessageSender.builder(JsHandler.HB_updateLocationData).
                            send((count++) + msgContent, null);
                }

                @Override
                public void updateStatus(String provider, int status, Bundle extras) {

                }

                @Override
                public void updateGpsStatus(GpsStatus gpsStatus) {

                }
            });
        } else if (event.handler.equals(JsHandler.HB_stopLoactionService)) { // TODO: 关闭定位功能
            YtLocationMgr.with(ytBaseFragment.getContext()).removeLocation();
            Location location = YtLocationMgr.with(ytBaseFragment.getContext()).currentLocation();

            LocationInfo info = new LocationInfo();
            info.setLongitude(location.getLongitude());
            info.setLatitude(location.getLatitude());
            info.setServiceFlag(false);
            Gson gson = new Gson();
            String msgContent = gson.toJson(info);
            event.onCallback(msgContent);
        }
    }
}
