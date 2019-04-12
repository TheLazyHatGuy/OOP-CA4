package com.ca4.Server.Cache;

import java.util.HashMap;

public class Cache {
    private static HashMap<CacheObject, String> movieTitleCache;
    private static HashMap<CacheObject, String> movieDirectorrCache;
    private static HashMap<CacheObject, String> movieGenreCache;

    public Cache() {
        movieTitleCache = new HashMap<>();
        movieDirectorrCache = new HashMap<>();
        movieGenreCache = new HashMap<>();
    }

    public void addToMovieTitleCache(String searchString, String jsonStringToCache) {
        CacheObject cacheObject = new CacheObject(CacheType.MOVIE_TITLE, searchString);
        movieTitleCache.put(cacheObject, jsonStringToCache);
    }

    public String queryMovieTitleCache(String searchString) {
        CacheObject cacheObject = new CacheObject(CacheType.MOVIE_TITLE, searchString);

        if (movieTitleCache.containsKey(cacheObject)) {
            System.out.println("Object has been cached");
            return movieTitleCache.get(cacheObject);
        } else {
            return "";
        }
    }
}
