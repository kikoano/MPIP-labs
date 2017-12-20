package com.example.kikoano111.lab4;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kikoano111 on 20/12/2017.
 */

public class GeometryLocation {
    @SerializedName("lat")
    @Expose
    public double lat;

    @SerializedName("lng")
    @Expose
    public double lng;
}
