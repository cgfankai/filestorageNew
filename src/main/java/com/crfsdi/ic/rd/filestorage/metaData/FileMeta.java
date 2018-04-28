package com.crfsdi.ic.rd.filestorage.metaData;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.UUID;

public class FileMeta implements DocUtil {
    private String _id;
    private String name;
    private String userId;
    private String type;
    private String profile;
    private String serviceId;
    private String projectId;
    private Integer authLevel;
    @JsonIgnore
    private String path;
    private Date date;


    public FileMeta() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public FileMeta set_id(String _id) {
        this._id = _id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FileMeta setName(String name) {
        this.name = name;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public FileMeta setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getType() {
        return type;
    }

    public FileMeta setType(String type) {
        this.type = type;
        return this;
    }

    public String getProfile() {
        return profile;
    }

    public FileMeta setProfile(String profile) {
        this.profile = profile;
        return this;
    }

    public String getServiceId() {
        return serviceId;
    }

    public FileMeta setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public FileMeta setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public Integer getAuthLevel() {
        return authLevel;
    }

    public FileMeta setAuthLevel(Integer authLevel) {
        this.authLevel = authLevel;
        return this;
    }

    public String getPath() {
        return path;
    }

    public FileMeta setPath(String path) {
        this.path = path;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public FileMeta setDate(Date date) {
        this.date = date;
        return this;
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", profile='" + profile + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", authLevel=" + authLevel +
                ", path='" + path + '\'' +
                ", date=" + date +
                '}';
    }
}
