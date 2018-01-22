package com.wanbox.bxweb.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author laihuan.wan
 * Created by laihuan.wan on 2018/01/13 0023.
 */

public class PhotoPreInfo extends BaseInfo{

    /**
     * position : 1 // TODO: 首次显示的图片索引
     * imageUrls : [{"imageUrl":"url"}] // TODO: 需要显示的图片 url 集合
     */

    private String position;
    private List<ImageUrlsBean> imageUrls;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<ImageUrlsBean> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<ImageUrlsBean> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public static class ImageUrlsBean{

        /**
         * imageUrl : url
         */

        private String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
