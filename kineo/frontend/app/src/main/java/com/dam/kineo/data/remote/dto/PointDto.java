package com.dam.kineo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class PointDto {
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("altitude")
    private double altitude;
    @SerializedName("timestamp")
    private long timestamp;

    public PointDto() {}

    public PointDto(double latitude, double longitude, double altitude, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timestamp = timestamp;
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public double getAltitude() { return altitude; }
    public void setAltitude(double altitude) { this.altitude = altitude; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
