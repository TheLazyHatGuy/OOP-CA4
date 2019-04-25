package com.ca4.Core;

public class MovieServiceDetails
{
    public static final int SERVER_PORT = 50000;
    public static final int MAX_CONNECTIONS = 3;
    public static final int CACHE_TTL = 60; //Cache Time To Live is done in seconds
    public static final String SERVER_IP = "127.0.0.1";
    public static final String BREAKING_CHARACTER = "%%";

    //Commands
    public static final String LOGIN                    = "HELO";
    public static final String REGISTER                 = "RJST";
    public static final String SEARCH_MOVIE_TITLE       = "SRX TTL";
    public static final String SEARCH_MOVIE_DIRECTOR    = "SRX TRKT";
    public static final String SEARCH_MOVIE_GENRE       = "SRX JNR";
    public static final String ADD_MOVIE                = "ADD";
    public static final String REMOVE_MOVIE             = "RMF";
    public static final String UPDATE_MOVIE             = "UPTT";
    public static final String WATCH_MOVIE              = "WX";
    public static final String RECOMMEND_MOVIE          = "RKMN";
    public static final String CLOSE_CONNECTION         = "BYE";

    //Responses
    public static final String LOGIN_SUCCESS = "WLKM";
    public static final String LOGIN_NOT_REGISTERED = "ERR 404";
    public static final String LOGIN_WRONG_INFO = "ERR 403";
    public static final String REGISTER_SUCCESS = "RJST";
    public static final String REGISTER_ALREADY_REGISTERED = "ERR ALRT RJST";
    public static final String ADD_SUCCESS = "SKSS";
    public static final String REMOVE_SUCCESS = "SKSS";
    public static final String UPDATE_SUCCESS = "SKSS";
    public static final String WATCH_SUCCESS = "SKSS";

    public static final String FAIL = "ERR";
    public static final String UNRECOGNISED_COMMAND = "ANKN";

    // ending characters
    public static final CharSequence JSONOBJECT_ENDINGCHAR = "}";
    public static final CharSequence JSONARRAY_ENDINGCHAR = "]";

    //Terminal Colours
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
}
