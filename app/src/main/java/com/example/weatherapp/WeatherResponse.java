package com.example.weatherapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("coord")
    @Expose
    public Coord coord;
    @SerializedName("sys")
    @Expose
    public Sys sys;
    @SerializedName("weather")
    @Expose
    public List<Weather> weather = null;
    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("rain")
    @Expose
    public Rain rain;
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("dt")
    @Expose
    public Double dt;
    @SerializedName("id")
    @Expose
    public Double id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("cod")
    @Expose
    public Double cod;

    public class Clouds {

        @SerializedName("all")
        @Expose
        public Double all;

    }
    public class Coord {

        @SerializedName("lon")
        @Expose
        public Double lon;
        @SerializedName("lat")
        @Expose
        public Double lat;

    }
    public class Main {

        @SerializedName("temp")
        @Expose
        public Double temp;
        @SerializedName("humidity")
        @Expose
        public Double humidity;
        @SerializedName("pressure")
        @Expose
        public Double pressure;
        @SerializedName("temp_min")
        @Expose
        public Double tempMin;
        @SerializedName("temp_max")
        @Expose
        public Double tempMax;

    }
    public class Rain {

        @SerializedName("3h")
        @Expose
        public Double _3h;

    }
    public class Sys {

        @SerializedName("country")
        @Expose
        public String country;
        @SerializedName("sunrise")
        @Expose
        public Double sunrise;
        @SerializedName("sunset")
        @Expose
        public Double sunset;

    }
    public class Weather {

        @SerializedName("id")
        @Expose
        public Double id;
        @SerializedName("main")
        @Expose
        public String main;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("icon")
        @Expose
        public String icon;

    }
    public class Wind {

        @SerializedName("speed")
        @Expose
        public Double speed;
        @SerializedName("deg")
        @Expose
        public Double deg;

    }

}