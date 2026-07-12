package com.dam.kineo.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "step_sessions")
public class StepSessionEntity {
    @PrimaryKey
    @NonNull
    public String id; // UUID de la sesión
    
    public String date;
    public int steps;
    public double calories;
    public double distanceMeters;
    public int activeMinutes;

    public StepSessionEntity() {
        this.id = java.util.UUID.randomUUID().toString();
    }
}
