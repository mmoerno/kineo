package com.dam.kineo.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "route_points")
public class RoutePointEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long sessionId;
    public double latitude;
    public double longitude;
    public double altitude;
    public long timestamp;

    public RoutePointEntity() {}

    @Ignore
    public RoutePointEntity(long id, long sessionId, double latitude, double longitude, double altitude, long timestamp) {
        this.id = id;
        this.sessionId = sessionId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getSessionId() { return sessionId; }
    public void setSessionId(long sessionId) { this.sessionId = sessionId; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public double getAltitude() { return altitude; }
    public void setAltitude(double altitude) { this.altitude = altitude; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
