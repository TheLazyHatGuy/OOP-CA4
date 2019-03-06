package com.ca4.DTO;

import org.json.JSONObject;

public class Movie
{
    private int id;
    private String title;
    private String genre;
    private String director;
    private String runtime;
    private String plot;
    private String location;
    private String poster;
    private String rating;
    private String format;
    private String year;
    private String starring;
    private int copies;
    private String barcode;
    private String userRating;

    public Movie(int id, String title, String genre, String director,
                 String runtime, String plot, String location, String poster,
                 String rating, String format, String year, String starring,
                 int copies, String barcode, String userRating)
    {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.runtime = runtime;
        this.plot = plot;
        this.location = location;
        this.poster = poster;
        this.rating = rating;
        this.format = format;
        this.year = year;
        this.starring = starring;
        this.copies = copies;
        this.barcode = barcode;
        this.userRating = userRating;
    }

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getGenre()
    {
        return genre;
    }

    public String getDirector()
    {
        return director;
    }

    public String getRuntime()
    {
        return runtime;
    }

    public String getPlot()
    {
        return plot;
    }

    public String getLocation()
    {
        return location;
    }

    public String getPoster()
    {
        return poster;
    }

    public String getRating()
    {
        return rating;
    }

    public String getFormat()
    {
        return format;
    }

    public String getYear()
    {
        return year;
    }

    public String getStarring()
    {
        return starring;
    }

    public int getCopies()
    {
        return copies;
    }

    public String getBarcode()
    {
        return barcode;
    }

    public String getUserRating()
    {
        return userRating;
    }

    @Override
    public String toString()
    {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", director='" + director + '\'' +
                ", runtime='" + runtime + '\'' +
                ", plot='" + plot + '\'' +
                ", location='" + location + '\'' +
                ", poster='" + poster + '\'' +
                ", rating='" + rating + '\'' +
                ", format='" + format + '\'' +
                ", year='" + year + '\'' +
                ", starring='" + starring + '\'' +
                ", copies=" + copies +
                ", barcode='" + barcode + '\'' +
                ", userRating='" + userRating + '\'' +
                '}';
    }

    public JSONObject toJSONObject()
    {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", this.id);
        jsonObject.put("title", this.title);
        jsonObject.put("genre", this.genre);
        jsonObject.put("director", this.director);
        jsonObject.put("runtime", this.runtime);
        jsonObject.put("plot", this.plot);
        jsonObject.put("location", this.location);
        jsonObject.put("poster", poster);
        jsonObject.put("rating", this.rating);
        jsonObject.put("format", this.format);
        jsonObject.put("year", this.year);
        jsonObject.put("staring", this.starring);
        jsonObject.put("copies", this.copies);
        jsonObject.put("barcode", this.barcode);
        jsonObject.put("user-rating", this.userRating);

        return jsonObject;
    }

    public String toJSONString()
    {
        return toJSONObject().toString(4);
    }

    public String toJSONString(int indentFactor)
    {
        return toJSONObject().toString(indentFactor);
    }
}
