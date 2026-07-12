package com.dam.kineo.domain.model;

public class Badge {
    private String id;
    private String name;
    private String description;
    private String iconRes;
    private Long unlockedAt;
    private boolean isUnlocked;

    public Badge() {}

    public Badge(String id, String name, String description, String iconRes, Long unlockedAt, boolean isUnlocked) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconRes = iconRes;
        this.unlockedAt = unlockedAt;
        this.isUnlocked = isUnlocked;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
