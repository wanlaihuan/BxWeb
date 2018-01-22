package com.wanbox.bxweb.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.wanbox.bxweb.event.BxwebJsMessageProcessor;
import com.wanbox.bxweb.util.H5Box;
import com.yingt.uimain.base.BaseFragmentActivity;
import com.yingt.uimain.util.UiMain;

/**
 * @author laihuan.wan
 *         Created by laihuan.wan on 2018/01/13 0017.
 */

public class YtBrowserActivity extends BaseFragmentActivity {

    private BrowserFragment browserFragment;
    private BxwebJsMessageProcessor bxwebJsMessageProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = H5Box.with().getBundle();//getIntent().getExtras();
        browserFragment = new BrowserFragment();
        setStatusBarStyle(bundle.getBoolean(BrowserFragment.PARAM_STATUS_BAR_SHOW, true));
        browserFragment.setTitleVisibility(bundle.getBoolean(BrowserFragment.PARAM_TITLE_SHOW, true));
        boolean isSetTitleBarColor = bundle.getBoolean(BrowserFragment.PARAM_IS_SET_TITLE_BAR_COLOR, false);
        if (isSetTitleBarColor) {
            browserFragment.setToolbarBackgroundColor(bundle.getInt(BrowserFragment.PARAM_STATUS_BAR_COLOR, -1));
        }
        UiMain.with(this).loadV4Fragment(browserFragment);

        Object object = UiMain.getBindingTempDatas();
        if (object != null && object instanceof BxwebJsMessageProcessor) {
            bxwebJsMessageProcessor = (BxwebJsMessageProcessor) object;
            bxwebJsMessageProcessor.setBrowserFragment(browserFragment);
            UiMain.bindingTempDatas(null);
        }

    }

    @Override
    public void onBackPressed() {
        if (browserFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void setStatusBarStyle(boolean statusBarStyle) {
        if (statusBarStyle) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }
        }
        browserFragment.setTranslucentBars(statusBarStyle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bxwebJsMessageProcessor != null) {
            bxwebJsMessageProcessor.setOnEventListener(null);
            bxwebJsMessageProcessor.onDestroy();
        }
    }
}
