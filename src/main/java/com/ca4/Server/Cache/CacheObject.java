package com.ca4.Server.Cache;

import java.util.Date;

public class CacheObject {
    private Date timestamp;
    private String jsonString;

    CacheObject(String jsonString) {
        this.timestamp = new Date();
        this.jsonString = jsonString;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setTimestamp() {
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "CacheObject{" +
                ", timestamp=" + timestamp +
                ", jsonString='" + jsonString + '\'' +
                '}';
    }
}
