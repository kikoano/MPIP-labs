package com.example.kikoano111.lab3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kikoano111 on 6/12/2017.
 */

public class JsonDataLong {
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Released")
    @Expose
    private String year;
    @SerializedName("Plot")
    @Expose
    private String plot;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }
}
