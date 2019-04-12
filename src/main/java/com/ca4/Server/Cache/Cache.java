package com.ca4.Server.Cache;

import com.ca4.Core.MovieServiceDetails;

import java.util.Calendar;
import java.util.Date;
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

    public void checkAllCaches() {
        checkCacheTTL(movieTitleCache);
        checkCacheTTL(movieDirectorrCache);
        checkCacheTTL(movieGenreCache);
    }

    /**
     * This function will compare the time stamps of all searches in the cache and the defined TTL for the cache.
     * If the timestamp is passed the TTL for the cache, the cached item will be removed
     */
    private void checkCacheTTL(HashMap<String, CacheObject> cacheToCheck) {
        //Method for adding time taken from - https://www.tutorialspoint.com/javaexamples/date_add_time.htm
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        for (String cacheKey : cacheToCheck.keySet()) {
            CacheObject cacheObject = cacheToCheck.get(cacheKey);

            Calendar objectCalendar = Calendar.getInstance();
            objectCalendar.setTime(cacheObject.getTimestamp());
            objectCalendar.add(Calendar.SECOND, MovieServiceDetails.CACHE_TTL);

            //If the timestamp + TTL is before the current time, the cached item must be deleted
            if (objectCalendar.before(calendar)) {
                cacheToCheck.remove(cacheKey);
            }
        }
    }
}
