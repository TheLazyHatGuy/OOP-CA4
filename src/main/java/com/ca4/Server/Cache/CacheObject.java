package com.ca4.Server.Cache;

import java.util.Date;

public class CacheObject {
    private Date timestamp;
    private String jsonString;

    CacheObject(String jsonString) {
        this.timestamp = new Date();
        this.jsonString = jsonString;
    }

    Date getTimestamp() {
        return timestamp;
    }

    String getJsonString() {
        return jsonString;
    }

    void setTimestamp() {
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
