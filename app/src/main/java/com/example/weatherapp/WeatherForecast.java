package com.example.weatherapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherForecast {

    @SerializedName("cod")
    @Expose
    public String cod;
    @SerializedName("message")
    @Expose
    public Double message;
    @SerializedName("cnt")
    @Expose
    public Double cnt;
    @SerializedName("list")
    @Expose
    public java.util.List<List> list = null;
    @SerializedName("city")
    @Expose
    public City city;
    public class City {

        @SerializedName("id")
        @Expose
        public Double id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("coord")
        @Expose
        public Coord coord;
        @SerializedName("country")
        @Expose
        public String country;

    }
    public class Clouds {

        @SerializedName("all")
        @Expose
        public Double all;

    }
    public class Coord {

        @SerializedName("lat")
        @Expose
        public Double lat;
        @SerializedName("lon")
        @Expose
        public Double lon;

    }
    public class List {

        @SerializedName("dt")
        @Expose
        public Double dt;
        @SerializedName("main")
        @Expose
        public Main main;
        @SerializedName("weather")
        @Expose
        public java.util.List<Weather> weather = null;
        @SerializedName("clouds")
        @Expose
        public Clouds clouds;
        @SerializedName("wind")
        @Expose
        public Wind wind;
        @SerializedName("rain")
        @Expose
        public Rain rain;
        @SerializedName("sys")
        @Expose
        public Sys sys;
        @SerializedName("dt_txt")
        @Expose
        public String dtTxt;

    }
    public class Main {

        @SerializedName("temp")
        @Expose
        public Double temp;
        @SerializedName("temp_min")
        @Expose
        public Double tempMin;
        @SerializedName("temp_max")
        @Expose
        public Double tempMax;
        @SerializedName("pressure")
        @Expose
        public Double pressure;
        @SerializedName("sea_level")
        @Expose
        public Double seaLevel;
        @SerializedName("grnd_level")
        @Expose
        public Double grndLevel;
        @SerializedName("humidity")
        @Expose
        public Double humidity;
        @SerializedName("temp_kf")
        @Expose
        public Double tempKf;

    }
    public class Rain {


    }

    public class Sys {

        @SerializedName("pod")
        @Expose
        public String pod;

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
