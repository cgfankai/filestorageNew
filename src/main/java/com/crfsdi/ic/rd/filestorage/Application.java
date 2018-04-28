package com.crfsdi.ic.rd.filestorage;

import com.crfsdi.ic.rd.filestorage.Data.Util;
import com.crfsdi.ic.rd.filestorage.metaData.DocUtil;
import com.crfsdi.ic.rd.filestorage.metaData.ProjectMeta;
import com.crfsdi.ic.rd.filestorage.metaData.ServiceMeta;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private MongoClient mongoClient;

    @Bean
    public MongoClient mongoClient() {
        LOG.info("===========================初始化MongoDB,创建index===================");
        MongoClient mongoClient = new MongoClient(Util.getMongoHost(), Util.getMongoPort());
        mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_FILE_NAME)
                .createIndex(Indexes.ascending("serviceId","name"));
        mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_SERVICE_NAME)
                .createIndex(Indexes.ascending("name"));
        mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_PROJECT_NAME)
                .createIndex(Indexes.ascending("name"));
        return mongoClient;
    }

    @Bean(name = "servicesBean")
    @RequestScope
    public Map<String, ServiceMeta> serviceMetaMap() {
        Map<String, ServiceMeta> result = new HashMap<>();
        MongoCollection<? extends Document> collection = mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_SERVICE_NAME);
        collection.find().forEach((Block<Document>) item -> {
            result.put(item.getString("_id"), DocUtil.getThis(item, ServiceMeta.class));
        });
        return result;
    }

    @Bean(name = "projectsBean")
    @RequestScope
    public Map<String, ProjectMeta> projectMetaMap() {

        Map<String, ProjectMeta> result = new HashMap<>();
        MongoCollection<? extends Document> collection = mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_PROJECT_NAME);
        collection.find().forEach((Block<Document>) item -> {
            result.put(item.getString("_id"), DocUtil.getThis(item, ProjectMeta.class));
        });
        return result;
    }
}
