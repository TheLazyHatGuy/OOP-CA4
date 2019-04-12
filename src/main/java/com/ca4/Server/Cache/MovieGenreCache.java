package com.ca4.Server.Cache;

import java.util.Date;

public class MovieGenreCache {
    private CacheObject cacheObject;
    private String jsonToCache;

    public MovieGenreCache(String searchString, String jsonToCache) {
        this.cacheObject = new CacheObject(CacheType.MOVIE_GENRE, searchString);
        this.jsonToCache = jsonToCache;
    }

    public CacheObject getCacheObject() {
        return cacheObject;
    }

    public String getJsonToCache() {
        return jsonToCache;
    }

    public void updateTimestamp() {
        cacheObject.setTimestamp(new Date());
    }

    @Override
    public String toString() {
        return "MovieGenreCache{" +
                "cacheObject=" + cacheObject +
                ", jsonToCache='" + jsonToCache + '\'' +
                '}';
    }
}
