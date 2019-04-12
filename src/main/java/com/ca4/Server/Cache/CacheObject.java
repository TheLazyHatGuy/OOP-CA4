package com.ca4.Server.Cache;

import java.util.Date;

public class CacheObject {
    private CacheType cacheType;
    private Date timestamp;
    private String searchString;

    CacheObject(CacheType cacheType, String searchString) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheObject that = (CacheObject) o;
        return searchString.equals(that.searchString);
    }

    @Override
    public int hashCode() {
        return searchString.hashCode() + 64;
    }
}
