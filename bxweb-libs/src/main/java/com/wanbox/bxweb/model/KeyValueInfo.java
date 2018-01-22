package com.wanbox.bxweb.model;

/**
 * @author laihuan.wan
 * Created by laihuan.wan on 2018/01/13 0023.
 */

public class KeyValueInfo extends BaseInfo{
    /**
     * TODO: dataType : account 存储的 key
     * TODO: dataValue : 1234567890 存储的值
     */
    private String dataType;
    private String dataValue;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }
}
