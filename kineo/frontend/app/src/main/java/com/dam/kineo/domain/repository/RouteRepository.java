package com.dam.kineo.domain.repository;

import com.dam.kineo.data.remote.dto.PointDto;

import java.util.List;

public interface RouteRepository {
    void syncRoute(String token, String sessionId, List<PointDto> points);
    void fetchRoute(String token, String sessionId);
}
