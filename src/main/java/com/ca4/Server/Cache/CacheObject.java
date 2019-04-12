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

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CacheObject{" +
                ", timestamp=" + timestamp +
                ", jsonString='" + jsonString + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheObject that = (CacheObject) o;
        return jsonString.equals(that.jsonString);
    }

    @Override
    public int hashCode() {
        return jsonString.hashCode() + 64;
    }
}
