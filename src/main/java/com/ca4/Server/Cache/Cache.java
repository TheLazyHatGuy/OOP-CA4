package com.ca4.Server.Cache;

import com.ca4.Core.MovieServiceDetails;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Cache {
    private static HashMap<String, CacheObject> movieTitleCache;
    private static HashMap<String, CacheObject> movieDirectorCache;
    private static HashMap<String, CacheObject> movieGenreCache;

    public Cache() {
        movieTitleCache = new HashMap<>();
        movieDirectorCache = new HashMap<>();
        movieGenreCache = new HashMap<>();
    }

    /**
     * Adds a search and JSON string to the HashMap cache
     *
     * @param searchString      String the user has searched for
     * @param jsonStringToCache The JSON object string taken from the Movie DTO
     */
    public void addToMovieTitleCache(String searchString, String jsonStringToCache) {
        CacheObject cacheObject = new CacheObject(jsonStringToCache);
        movieTitleCache.put(searchString.toLowerCase(), cacheObject);
    }

    /**
     * Checks if a tit;e search has been cached previously
     * @param searchString String the user has searched for
     * @return A JSON Object string if the search has been cached previously
     */
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

    /**
     * Adds a search and JSON string to the HashMap cache
     * @param searchString String the user has searched for
     * @param jsonStringToCache The JSON array string taken from the Movie DTO
     */
    public void addToMovieDirectorCache(String searchString, String jsonStringToCache) {
        CacheObject cacheObject = new CacheObject(jsonStringToCache);
        movieDirectorCache.put(searchString.toLowerCase(), cacheObject);
    }

    /**
     * Checks if a director search has been cached previously
     * @param searchString String the user has searched for
     * @return A JSON Array string if the search has been cached previously
     */
    public String queryMovieDirectorCache(String searchString) {
        if (movieDirectorCache.containsKey(searchString.toLowerCase())) {
            System.out.println("Object has been cached");
            CacheObject cacheObject = movieDirectorCache.get(searchString.toLowerCase());
            cacheObject.setTimestamp();
            return cacheObject.getJsonString();
        } else {
            return "";
        }
    }

    /**
     * Adds a search and JSON string to the HashMap cache
     * @param searchString String the user has searched for
     * @param jsonStringToCache The JSON array string taken from the Movie DTO
     */
    public void addToMovieGenreCache(String searchString, String jsonStringToCache) {
        CacheObject cacheObject = new CacheObject(jsonStringToCache);
        movieGenreCache.put(searchString.toLowerCase(), cacheObject);
    }

    /**
     * Checks if a genre search has been cached previously
     * @param searchString String the user has searched for
     * @return A JSON Array string if the search has been cached previously
     */
    public String queryMovieGenreCache(String searchString) {
        if (movieGenreCache.containsKey(searchString.toLowerCase())) {
            System.out.println("Object has been cached");
            CacheObject cacheObject = movieGenreCache.get(searchString.toLowerCase());
            cacheObject.setTimestamp();
            return cacheObject.getJsonString();
        } else {
            return "";
        }
    }

    /**
     * Runs the cache TLL check for all of the HashMap caches
     */
    public void checkAllCaches() {
        checkCacheTTL(movieTitleCache);
        checkCacheTTL(movieDirectorCache);
        checkCacheTTL(movieGenreCache);
    }

    /**
     * This function will compare the time stamps of all searches in the cache and the defined TTL for the cache.
     *      * If the timestamp is passed the TTL for the cache, the cached item will be removed
     * @param cacheToCheck The HashMap of the cache to be checked
     */
    private void checkCacheTTL(HashMap<String, CacheObject> cacheToCheck) {
        //Method for adding time taken from - https://www.tutorialspoint.com/javaexamples/date_add_time.htm
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        for (String cacheKey : cacheToCheck.keySet()) {
            CacheObject cacheObject = cacheToCheck.get(cacheKey);

            Calendar objectCalendar = Calendar.getInstance();
            objectCalendar.setTime(cacheObject.getTimestamp());
            objectCalendar.add(Calendar.HOUR, MovieServiceDetails.CACHE_TTL);

            //If the timestamp + TTL is before the current time, the cached item must be deleted
            if (objectCalendar.before(calendar)) {
                cacheToCheck.remove(cacheKey);
            }
        }
    }
}
