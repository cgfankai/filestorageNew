package com.crfsdi.ic.rd.filestorage.metaData;

import java.util.UUID;

public class ServiceMeta implements DocUtil {
    private String _id;
    private String name;
    private String profile;

    public ServiceMeta() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public ServiceMeta set_id(String _id) {
        this._id = _id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceMeta setName(String name) {
        this.name = name;
        return this;
    }

    public String getProfile() {
        return profile;
    }

    public ServiceMeta setProfile(String profile) {
        this.profile = profile;
        return this;
    }

    @Override
    public String toString() {
        return "ServiceMeta{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
