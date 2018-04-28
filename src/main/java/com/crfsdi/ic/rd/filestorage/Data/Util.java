package com.crfsdi.ic.rd.filestorage.Data;

import com.crfsdi.ic.rd.filestorage.metaData.DocUtil;
import com.crfsdi.ic.rd.filestorage.metaData.FileMeta;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Util {
    private static String mongoHost = "localhost";
    private static int mongoPort = 27017;
    public static final String DATABAST_NAME = "crfsdi_fs";
    public static final String COLLECTION_FILE_NAME = "file";
    public static final String COLLECTION_SERVICE_NAME = "service";
    public static final String COLLECTION_PROJECT_NAME = "project";
    public static String rootDirect = "uploadFiles" + File.separator;
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    /**
     *
     * @param multipartFile
     * @return 文件存储的相对路径
     */
    public static String  saveFile(MultipartFile multipartFile) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String path = getNowPath();
        executorService.execute(() -> {
            File file = new File(rootDirect + path);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            BufferedOutputStream bufferedOutputStream = null;
            try{
                if (!file.exists()){
                    file.createNewFile();
                }
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                bufferedOutputStream.write(multipartFile.getBytes());
                bufferedOutputStream.flush();
                LOG.info("存储文件完毕==================================");
            } catch (IOException e) {
                e.printStackTrace();
                LOG.info(e.getMessage());
            }finally {
                if (bufferedOutputStream != null){
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        return path;
    }
    private static  String getNowPath(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return new StringBuilder()
                .append(localDateTime.getYear())
                .append(localDateTime.getMonthValue() > 9 ? localDateTime.getMonthValue() : "0" + localDateTime.getMonthValue())
                .append(localDateTime.getDayOfMonth() > 9 ? localDateTime.getDayOfMonth() : "0" + localDateTime.getDayOfMonth())
                .append(File.separator)
                .append(localDateTime.getHour() > 9 ? localDateTime.getHour() : "0" + localDateTime.getHour())
                .append(File.separator)
                .append(UUID.randomUUID().toString())
                .toString();
    }

    public static String getMongoHost() {
        return mongoHost;
    }

    public static int getMongoPort() {
        return mongoPort;
    }


    public static ResponseEntity<byte[]> getFile(Document fileDoc, String  userAgent){
        if (fileDoc == null){
            return ResponseEntity.status(404).header("message","not found file").build();
        }else {
            try{
                FileMeta fileMeta = DocUtil.getThis(fileDoc,FileMeta.class);
                if (userAgent.toLowerCase().contains("msie")){
                    fileMeta.setName(URLEncoder.encode(fileMeta.getName(),"utf-8"));
                }else {
                    fileMeta.setName(new String(fileMeta.getName().getBytes(),"ISO-8859-1"));
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(Util.rootDirect + fileMeta.getPath()));
                byte[] binaryData = bufferedInputStream.readAllBytes();
                bufferedInputStream.close();
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\"" + fileMeta.getName()+"\"")
                        .body(binaryData);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok().build();
        }
    }
}
