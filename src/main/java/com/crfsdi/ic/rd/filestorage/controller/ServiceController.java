package com.crfsdi.ic.rd.filestorage.controller;

import com.crfsdi.ic.rd.filestorage.Data.Util;
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
public class ServiceController {
    private static Logger LOG = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    MongoClient mongoClient;

    @Autowired
    Map<String, ServiceMeta> serviceMetaMap;

    @GetMapping("/service")
    public Collection<ServiceMeta> getAllService(){
        return serviceMetaMap.values();
    }
    @PostMapping("/service")
    public String addService(ServiceMeta serviceMetaInput){
        try {
            mongoClient.getDatabase(Util.DATABAST_NAME)
                    .getCollection(Util.COLLECTION_SERVICE_NAME)
                    .insertOne(serviceMetaInput.toDocument());
            return serviceMetaInput.get_id();
        }catch (MongoException misconception){
            LOG.info(misconception.getMessage());
        }
        return "";
    }
    @GetMapping("/service/{id}")
    public ServiceMeta getServiceById(@PathVariable("id") String id){
        if (serviceMetaMap.containsKey(id)){
            return serviceMetaMap.get(id);
        }else {
            return null;
        }

    }
    @PostMapping ("/service/{id}")
    public boolean updateServiceById(ServiceMeta serviceMeta,@PathVariable("id") String serviceId){
        serviceMeta.set_id(serviceId);
        mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_SERVICE_NAME)
                .findOneAndReplace(Filters.eq(serviceId),serviceMeta.toDocument());
        return true;
    }
    @GetMapping("/deleteservice")
    public boolean deleteServiceById(@RequestParam("id")String serviceId){
        mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_SERVICE_NAME)
                .deleteOne(Filters.eq(serviceId));
        return true;
    }
}
