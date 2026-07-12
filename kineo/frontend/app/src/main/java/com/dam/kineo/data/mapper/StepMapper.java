package com.dam.kineo.data.mapper;

import com.dam.kineo.data.local.entity.StepSessionEntity;
import com.dam.kineo.data.remote.dto.StepSessionDto;
import com.dam.kineo.domain.model.StepSession;

public final class StepMapper {

    private StepMapper() {}

    public static StepSession toDomain(StepSessionEntity entity) {
        if (entity == null) return null;
        return new StepSession(
                entity.id,
                entity.date,
                entity.steps,
                entity.calories,
                entity.distanceMeters,
                entity.activeMinutes
        );
    }

    public static StepSessionEntity toEntity(StepSession model) {
        if (model == null) return null;
        StepSessionEntity entity = new StepSessionEntity();
        entity.id = model.getId();
        entity.date = model.getDate();
        entity.steps = model.getSteps();
        entity.calories = model.getCalories();
        entity.distanceMeters = model.getDistanceMeters();
        entity.activeMinutes = model.getActiveMinutes();
        return entity;
    }

    public static StepSession dtoToDomain(StepSessionDto dto) {
        if (dto == null) return null;
        return new StepSession(
                dto.getId(),
                dto.getDate(),
                dto.getSteps(),
                dto.getCalories(),
                dto.getDistanceMeters(),
                dto.getActiveMinutes()
        );
    }
}
