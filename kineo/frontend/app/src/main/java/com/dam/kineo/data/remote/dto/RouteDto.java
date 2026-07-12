package com.dam.kineo.data.remote.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RouteDto {
    @SerializedName("sessionId")
    private String sessionId;
    @SerializedName("points")
    private List<PointDto> points;

    public RouteDto() {}

    public RouteDto(String sessionId, List<PointDto> points) {
        this.sessionId = sessionId;
        this.points = points;
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public List<PointDto> getPoints() { return points; }
    public void setPoints(List<PointDto> points) { this.points = points; }
}
