package com.dam.kineo.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "badges")
public class BadgeEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String description;
    public String iconRes;
    public Long unlockedAt;
    public boolean isUnlocked;

    public BadgeEntity() {}

    @Ignore
    public BadgeEntity(@NonNull String id, String name, String description, String iconRes, Long unlockedAt, boolean isUnlocked) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconRes = iconRes;
        this.unlockedAt = unlockedAt;
        this.isUnlocked = isUnlocked;
    }

    @NonNull public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIconRes() { return iconRes; }
    public void setIconRes(String iconRes) { this.iconRes = iconRes; }
    public Long getUnlockedAt() { return unlockedAt; }
    public void setUnlockedAt(Long unlockedAt) { this.unlockedAt = unlockedAt; }
    public boolean isUnlocked() { return isUnlocked; }
    public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
}
