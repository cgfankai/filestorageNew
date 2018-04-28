package com.crfsdi.ic.rd.filestorage.metaData;

import com.crfsdi.ic.rd.filestorage.output.FileOutput;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.StreamSupport;

public class DocUtilTest {

    @org.junit.Test
    public void toDocument() {
        FileMeta fileMeta = new FileMeta();
        Document document = fileMeta.setName("name")
                .set_id("0000000000000000000000")
                .setDate(new Date())
                .setUserId("1212").toDocument();
        MongoClient mongoClient = new MongoClient();
        mongoClient.getDatabase("test").getCollection("test").drop();
        mongoClient.getDatabase("test").getCollection("test").insertOne(document);
        System.out.println(document);
        document = mongoClient.getDatabase("test").getCollection("test").find().first();
        System.out.println(document);

    }

    @Test
    public void getThis() {
        FileMeta fileMeta = new FileMeta();
        Document document = fileMeta.setName("name")
                .set_id("0000000000000000000000")
                .setDate(new Date())
                .setUserId("1212").toDocument();
        FileMeta fileMeta1 = DocUtil.getThis(document,FileMeta.class);
        System.out.println(fileMeta1);
    }

    @Test
    public void testChileReflect(){
        FileOutput fileOutput = new FileOutput();
        Arrays.stream(fileOutput.getClass().getFields())
                .forEach(x->{
                    System.out.println(x.getName());
                });
        Arrays.stream(Object.class.getFields())
                .forEach(x->{
                    System.out.println(x.getName());
                });
    }

    @Test
    public void test(){
        Document document = new Document("1",1)
                .append("1",2)
                .append("1",3);
        System.out.println(document);
    }
    @Test
    public void getInsterestRate(){
        double principal = 4000;
        double insterest = 112.5;
        double yearInsterest = insterest * 365;
        double insterestRate = yearInsterest / principal;
        System.out.println(insterestRate);
    }
}