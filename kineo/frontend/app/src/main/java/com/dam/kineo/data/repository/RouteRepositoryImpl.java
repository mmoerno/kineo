package com.dam.kineo.data.repository;

import androidx.annotation.NonNull;

import com.dam.kineo.data.local.KineoDatabase;
import com.dam.kineo.data.local.dao.RoutePointDao;
import com.dam.kineo.data.local.entity.RoutePointEntity;
import com.dam.kineo.data.remote.KineoApiService;
import com.dam.kineo.data.remote.dto.PointDto;
import com.dam.kineo.data.remote.dto.RouteDto;
import com.dam.kineo.domain.repository.RouteRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteRepositoryImpl implements RouteRepository {
    private final RoutePointDao routePointDao;
    private final KineoApiService api;

    @Inject
    public RouteRepositoryImpl(RoutePointDao routePointDao, KineoApiService api) {
        this.routePointDao = routePointDao;
        this.api = api;
    }

    @Override
    public void syncRoute(String token, String sessionId, List<PointDto> points) {
        RouteDto route = new RouteDto(sessionId, points);
        api.syncRoute("Bearer " + token, route).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                // No further action required on success.
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public void fetchRoute(String token, String sessionId) {
        api.getRoute("Bearer " + token, sessionId).enqueue(new Callback<List<PointDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<PointDto>> call,
                                   @NonNull Response<List<PointDto>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                long localSessionId;
                try {
                    localSessionId = Long.parseLong(sessionId);
                } catch (NumberFormatException e) {
                    return;
                }

                List<RoutePointEntity> entities = new ArrayList<>();
                for (PointDto point : response.body()) {
                    RoutePointEntity entity = new RoutePointEntity();
                    entity.sessionId = localSessionId;
                    entity.latitude = point.getLatitude();
                    entity.longitude = point.getLongitude();
                    entity.altitude = point.getAltitude();
                    entity.timestamp = point.getTimestamp();
                    entities.add(entity);
                }

                KineoDatabase.databaseWriteExecutor.execute(() -> routePointDao.insertPoints(entities));
            }

            @Override
            public void onFailure(@NonNull Call<List<PointDto>> call, @NonNull Throwable t) {
            }
        });
    }
}
