package com.ca4.DTO;

import org.json.JSONObject;

public class WatchedMovie
{
    private int userID;
    private int movieID;

    public WatchedMovie(int userID, int movieID)
    {
        this.userID = userID;
        this.movieID = movieID;
    }

    public int getUserID()
    {
        return userID;
    }

    public int getMovieID()
    {
        return movieID;
    }

    @Override
    public String toString()
    {
        return "WatchedMovie{" +
                "userID=" + userID +
                ", movieID=" + movieID +
                '}';
    }

    public JSONObject toJSONObject()
    {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("user-id", this.userID);
        jsonObject.put("movie-id", this.movieID);

        return jsonObject;
    }

    public String toJSONString(){
        return toJSONObject().toString(4);
    }

    public String toJSONString(int indentFactor){
        return toJSONObject().toString(indentFactor);
    }
}
