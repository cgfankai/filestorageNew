package com.crfsdi.ic.rd.filestorage.output;

import com.crfsdi.ic.rd.filestorage.metaData.FileMeta;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class FileOutput extends FileMeta {
    private String serviceName;
    private String projectName;

    public FileOutput() {
        super();
    }

    public FileOutput(FileMeta fileMeta) {
        this.set_id(fileMeta.get_id())
                .setPath(fileMeta.getPath())
                .setAuthLevel(fileMeta.getAuthLevel())
                .setProjectId(fileMeta.getProjectId())
                .setServiceId(fileMeta.getServiceId())
                .setProfile(fileMeta.getProfile())
                .setType(fileMeta.getType())
                .setUserId(fileMeta.getUserId())
                .setDate(fileMeta.getDate())
                .setName(fileMeta.getName());
    }

    public String getServiceName() {
        return serviceName;
    }

    public FileOutput setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public FileOutput setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    @Override
    public String toString() {
        return "FileOutput{" +
                "serviceName='" + serviceName + '\'' +
                ", projectName='" + projectName + '\'' +
                "} " + super.toString();
    }
}
