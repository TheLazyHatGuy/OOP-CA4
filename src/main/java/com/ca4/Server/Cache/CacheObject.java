package com.ca4.Server.Cache;

import java.util.Date;

public class CacheObject {
    private CacheType cacheType;
    private Date timestamp;
    private String searchString;

    public CacheObject(CacheType cacheType, String searchString) {
        this.cacheType = cacheType;
        this.timestamp = new Date();
        this.searchString = searchString;
    }

    public CacheType getCacheType() {
        return cacheType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CacheObject{" +
                "cacheType=" + cacheType +
                ", timestamp=" + timestamp +
                ", searchString='" + searchString + '\'' +
                '}';
    }
}
