package com.wanbox.bxweb.app;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.wanbox.bxweb.H5BoxInit;


/**
 * Created by laihuan.wan on 2017/5/11 0011.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // TODO: 初始化 H5Box
        H5BoxInit.init(this);
    }
}
