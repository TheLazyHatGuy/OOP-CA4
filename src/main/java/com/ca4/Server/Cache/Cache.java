package com.ca4.Server.Cache;

import java.util.HashMap;

public class Cache {
    private static HashMap<String, CacheObject> movieTitleCache;
    private static HashMap<String, CacheObject> movieDirectorrCache;
    private static HashMap<String, CacheObject> movieGenreCache;

    public Cache() {
        movieTitleCache = new HashMap<>();
        movieDirectorrCache = new HashMap<>();
        movieGenreCache = new HashMap<>();
    }

    public void addToMovieTitleCache(String searchString, String jsonStringToCache) {
        CacheObject cacheObject = new CacheObject(jsonStringToCache);
        movieTitleCache.put(searchString.toLowerCase(), cacheObject);
    }

    public String queryMovieTitleCache(String searchString) {
        if (movieTitleCache.containsKey(searchString.toLowerCase())) {
            System.out.println("Object has been cached");
            CacheObject cacheObject = movieTitleCache.get(searchString.toLowerCase());
            cacheObject.setTimestamp();
            return cacheObject.getJsonString();
        } else {
            return "";
        }
    }
}
