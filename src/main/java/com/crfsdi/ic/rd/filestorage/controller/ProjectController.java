package com.crfsdi.ic.rd.filestorage.controller;

import com.crfsdi.ic.rd.filestorage.Data.Util;
import com.crfsdi.ic.rd.filestorage.metaData.ProjectMeta;
import com.crfsdi.ic.rd.filestorage.metaData.ServiceMeta;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.model.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
public class ProjectController {
    private static Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    MongoClient mongoClient;

    @Autowired
    Map<String, ProjectMeta> projectMetaMap;

    @GetMapping("/project")
    public Collection<ProjectMeta> getAllService(){
        return projectMetaMap.values();
    }
    @PostMapping("/project")
    public String getAllProject(ProjectMeta projectMeta){
        try {
            mongoClient.getDatabase(Util.DATABAST_NAME)
                    .getCollection(Util.COLLECTION_PROJECT_NAME)
                    .insertOne(projectMeta.toDocument());
            return projectMeta.get_id();
        }catch (MongoException misconception){
            LOG.info(misconception.getMessage());
        }
        return "";
    }
    @GetMapping("/project/{id}")
    public ProjectMeta getProjectById(@PathVariable("id") String id){
        if (projectMetaMap.containsKey(id)){
            return projectMetaMap.get(id);
        }else {
            return null;
        }

    }
    @PostMapping ("/project/{id}")
    public boolean updateProjectById(ProjectMeta projectMeta,@PathVariable("id") String serviceId){
        projectMeta.set_id(serviceId);
        mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_PROJECT_NAME)
                .findOneAndReplace(Filters.eq(serviceId),projectMeta.toDocument());
        return true;
    }
    @GetMapping("/deleteproject")
    public boolean deleteProjectById(@RequestParam("id")String serviceId){
        mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_PROJECT_NAME)
                .deleteOne(Filters.eq(serviceId));
        return true;
    }
}
