package com.hudfs.hudfs28.dtos;

public class AvatarRequest {
    private String name;     // for create
    private Long avatarId;   // for select/update

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getAvatarId() { return avatarId; }
    public void setAvatarId(Long avatarId) { this.avatarId = avatarId; }
}
