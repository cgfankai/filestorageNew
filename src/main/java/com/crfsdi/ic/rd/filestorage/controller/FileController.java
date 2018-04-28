package com.crfsdi.ic.rd.filestorage.controller;

import com.crfsdi.ic.rd.filestorage.Data.Util;
import com.crfsdi.ic.rd.filestorage.metaData.DocUtil;
import com.crfsdi.ic.rd.filestorage.metaData.FileMeta;
import com.crfsdi.ic.rd.filestorage.metaData.ProjectMeta;
import com.crfsdi.ic.rd.filestorage.metaData.ServiceMeta;
import com.crfsdi.ic.rd.filestorage.output.FileOutput;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@RestController
@CrossOrigin
//@RequestMapping("/filestorage/api")
public class FileController {
    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);
    @Autowired
    MongoClient mongoClient;

    @Autowired
    Map<String, ServiceMeta> serviceMetaMap;

    @Autowired
    Map<String, ProjectMeta> projectMetaMap;

    @GetMapping("/file")
    public List<? extends DocUtil> getAllFilesInfo() {
        List<FileOutput> result = new ArrayList<>();
        MongoCollection<? extends Document> fileCollection = mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_FILE_NAME);
        fileCollection.find().forEach((Block<Document>) fileDoc -> {
            FileOutput fileOutput = new FileOutput(DocUtil.getThis(fileDoc, FileMeta.class));
            if (fileOutput.getServiceId() != null && serviceMetaMap.containsKey(fileOutput.getServiceId())) {
                fileOutput.setServiceName(serviceMetaMap.get(fileOutput.getServiceId()).getName());
            }
            if (fileOutput.getProjectId() != null && projectMetaMap.containsKey(fileOutput.getProjectId())) {
                fileOutput.setProjectName(projectMetaMap.get(fileOutput.getProjectId()).getName());
            }
            result.add(fileOutput);
        });
        return result;
    }

    @PostMapping("/file")
    public boolean fileUpload(@RequestParam("file") MultipartFile file
            , @RequestParam(value = "name", required = false) String name
            , @RequestParam(value = "serviceId", required = false) String serviceId
            , @RequestParam(value = "projectId", required = false) String projectId
            , @RequestParam(value = "type", required = false) String type
            , @RequestParam(value = "profile", required = false) String profile
            , @RequestParam(value = "userId", required = false) String userId) {
        if (!file.isEmpty()) {
            FileMeta fileMeta = new FileMeta()
                    .setPath(Util.saveFile(file))
                    .setName(name == null ? file.getOriginalFilename() : name)
                    .setServiceId(serviceId)
                    .setProjectId(projectId)
                    .setDate(new Date())
                    .setProfile(profile)
                    .setUserId(userId)
                    .setType(type);
            Document document = fileMeta.toDocument();
            LOG.info("文件meta{}", fileMeta);
            LOG.info("文件Document是{}", document);
            mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_FILE_NAME).insertOne(document);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable("id") String fileID, @RequestHeader("User-Agent") String userAgent) {
        Document fileDoc = mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_FILE_NAME)
                .find(Filters.eq(fileID)).first();
        return Util.getFile(fileDoc, userAgent);
    }

    @PostMapping("/file/{id}")
    public boolean updateFileById(@PathVariable("id") String fileID
            , @RequestParam(value = "name", required = false) String name
            , @RequestParam(value = "serviceId", required = false) String serviceId
            , @RequestParam(value = "projectId", required = false) String projectId
            , @RequestParam(value = "type", required = false) String type
            , @RequestParam(value = "profile", required = false) String profile) {
        Document fileDoc = mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_FILE_NAME)
                .find(Filters.eq(fileID)).first();
        if (fileDoc == null) {
            return false;
        } else {
            if (name != null && !name.isEmpty()) {
                fileDoc.append("name", name);
            }
            if (serviceId != null && !serviceId.isEmpty()) {
                fileDoc.append("serviceId", serviceId);
            }
            if (projectId != null && !projectId.isEmpty()) {
                fileDoc.append("projectId", projectId);
            }
            if (type != null && !type.isEmpty()) {
                fileDoc.append("type", type);
            }
            if (profile != null && !profile.isEmpty()) {
                fileDoc.append("profile", profile);
            }
            mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_FILE_NAME)
                    .findOneAndReplace(Filters.eq(fileID), fileDoc);
            return true;
        }
    }

    @GetMapping("/file/{serviceName}/{fileName}")
    public ResponseEntity<byte[]> getFileById(@PathVariable("serviceName") String serviceName,
                                              @PathVariable("fileName") String fileName,
                                              @RequestHeader("User-Agent") String userAgent) {
        Document document = mongoClient.getDatabase(Util.DATABAST_NAME)
                .getCollection(Util.COLLECTION_SERVICE_NAME)
                .find(
                        Filters.eq("name", serviceName)
                ).first();
        if (document != null) {
            String serviceId = document.getString("_id");
            Document fileDoc = mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_FILE_NAME)
                    .find(
                            Filters.and(
                                    Filters.eq("serviceId", serviceId)
                                    , Filters.eq("name", fileName)
                            )
                    ).first();
            return Util.getFile(fileDoc, userAgent);
        } else {
            return ResponseEntity.status(404).header("message", "not found file").build();
        }
    }

    @GetMapping("/deletefile")
    public boolean deleteFileById(@RequestParam("id") String fileId) {
        Optional<Document> documentOptional = Optional.ofNullable(mongoClient.getDatabase(Util.DATABAST_NAME)
                .getCollection(Util.COLLECTION_FILE_NAME)
                .find(Filters.eq(fileId)).first());
        documentOptional.ifPresent(
                document -> {
                    File file = new File(DocUtil.getThis(document, FileMeta.class).getPath());
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                }
        );
        mongoClient.getDatabase(Util.DATABAST_NAME).getCollection(Util.COLLECTION_FILE_NAME)
                .deleteOne(Filters.eq(fileId));
        return true;
    }

    @PostMapping("/searchfile")
    public List<FileOutput> searchFile(@RequestParam(value = "name", required = false) String inputName
            , @RequestParam(value = "serviceId", required = false) String inputServiceId
            , @RequestParam(value = "projectId", required = false) String inputProjectId) {
        List<FileOutput> result = new LinkedList<>();
        Bson filters = null;
        if (inputServiceId != null && inputProjectId != null) {
            filters = Filters.and(Filters.eq("serviceId", inputServiceId)
                    , Filters.eq("projectId", inputProjectId));
        }
        if (inputServiceId == null && inputProjectId != null) {
            filters = Filters.eq("projectId", inputProjectId);
        }
        if (inputServiceId != null && inputProjectId == null) {
            filters = Filters.eq("serviceId", inputServiceId);
        }
        mongoClient.getDatabase(Util.DATABAST_NAME)
                .getCollection(Util.COLLECTION_FILE_NAME)
                .find(filters)
                .forEach((Block<? super Document>) doc -> {
                    FileOutput temp = new FileOutput(DocUtil.getThis(doc,FileMeta.class));
                    if (temp.getServiceId() != null && serviceMetaMap.containsKey(temp.getServiceId())) {
                        temp.setServiceName(serviceMetaMap.get(temp.getServiceId()).getName());
                    }
                    if (temp.getProjectId() != null && projectMetaMap.containsKey(temp.getProjectId())) {
                        temp.setProjectName(projectMetaMap.get(temp.getProjectId()).getName());
                    }
                    if (inputName != null){
                        if (temp.getName().contains(inputName)){
                            result.add(temp);
                        }
                    }else {
                        result.add(temp);
                    }
                });
        LOG.info(result.toString());
        return result;
    }


}
