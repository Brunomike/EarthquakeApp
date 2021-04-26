package com.example.earthquakeapp;

public class Earthquake {
    // magnitude of the earthquake
    private double mMagnitude;

    // location of the earthquake
    private String mLocation;

    // Time of the earthquake
    private long mTimeInMilliseconds;

    // Website URL of the earthquake
    private String mUrl;

    public Earthquake(double magnitude, String location, long timeInMilliseconds, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }


    public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getUrl() {
        return mUrl;
    }
}
