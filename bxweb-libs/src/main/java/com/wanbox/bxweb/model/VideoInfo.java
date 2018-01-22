package com.wanbox.bxweb.model;

/**
 * @author laihuan.wan
 * Created by laihuan.wan on 2018/01/13 0024.
 *  {"title":"视频名","width":"300","yRatio":"0.4","image":"img","src":"src"}
 */

public class VideoInfo extends BaseInfo {
    /**
     * TODO: title : 视频名称
     * TODO: width : 300 视频宽度
     * TODO: yRatio : 0.4 视频 Y 坐标相对屏幕高度的一个系数
     * TODO: image : img 首张图 url
     * TODO: src : src 视频 url
     */
    private String title;
    private String width;
    private String yRatio;
    private String image;
    private String src;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getYRatio() {
        return yRatio;
    }

    public void setYRatio(String yRatio) {
        this.yRatio = yRatio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
