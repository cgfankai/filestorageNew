package com.crfsdi.ic.rd.filestorage.metaData;

import java.util.List;
import java.util.UUID;

public class ProjectMeta implements DocUtil {
    private String _id;
    private String name;
    private String profile;
    private List<String> members;

    public ProjectMeta() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public ProjectMeta set_id(String _id) {
        this._id = _id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProjectMeta setName(String name) {
        this.name = name;
        return this;
    }

    public String getProfile() {
        return profile;
    }

    public ProjectMeta setProfile(String profile) {
        this.profile = profile;
        return this;
    }

    public List<String> getMembers() {
        return members;
    }

    public ProjectMeta setMembers(List<String> members) {
        this.members = members;
        return this;
    }
}
