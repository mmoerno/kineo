package com.dam.kineo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class BadgeDto {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("icon_url")
    private String iconUrl;
    @SerializedName("unlocked_at")
    private Long unlockedAt;
    @SerializedName("is_unlocked")
    private boolean isUnlocked;

    public BadgeDto() {}

    public BadgeDto(String id, String name, String description, String iconUrl, Long unlockedAt, boolean isUnlocked) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.unlockedAt = unlockedAt;
        this.isUnlocked = isUnlocked;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    public Long getUnlockedAt() { return unlockedAt; }
    public void setUnlockedAt(Long unlockedAt) { this.unlockedAt = unlockedAt; }
    public boolean isUnlocked() { return isUnlocked; }
    public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
}
