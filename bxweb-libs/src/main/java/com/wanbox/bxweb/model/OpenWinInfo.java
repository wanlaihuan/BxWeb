package com.wanbox.bxweb.model;

/**
 * @author laihuan.wan
 * Created by laihuan.wan on 2018/01/13 0028.
 */

public class OpenWinInfo {

    /**
     * {"url":"https://www.baidu.com/"}
     * url : https://www.baidu.com/  TODO: 需要打开页面加载的 url 链接
     * showNativeTitle : true TODO: 是否显示原生标题栏
     *
     */

    private String url;

    private boolean showNativeTitle;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isShowNativeTitle() {
        return showNativeTitle;
    }

    public void setShowNativeTitle(boolean showNativeTitle) {
        this.showNativeTitle = showNativeTitle;
    }
}
