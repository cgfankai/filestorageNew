package com.crfsdi.ic.rd.filestorage.metaData;

import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public interface DocUtil {
    default public Document toDocument(){
        Document document = new Document();
        Class<? extends DocUtil> thisClass = this.getClass();
        Field[] fields = thisClass.getDeclaredFields();
        for (Field field : fields){
            try {
                field.setAccessible(true);
                document.append(field.getName(),field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return document;
    }

    static <T> T getThis(Document document, Class<T> clazz){
        T t = null;
        try {
            t = clazz.getConstructor(null).newInstance(null);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);
                field.set(t,document.get(field.getName()));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return t;
    }
}
