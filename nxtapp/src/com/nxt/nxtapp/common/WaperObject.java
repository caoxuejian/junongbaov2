package com.nxt.nxtapp.common;


import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * 把字段的值设置到对象里面。
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-8-30
 * Time: 10:32:44
 * To change this template use File | Settings | File Templates.
 */
public class WaperObject {

    public static void setObjectField(Object object, Map<String, Object> map) {
        Class clazz = object.getClass();
        Set requestParameterMap = map.keySet();    //得到所有参数。
        if (requestParameterMap == null) {
        }
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!requestParameterMap.contains(field.getName())) {
                continue;
            }
            Object fieldValueFromRequest = map.get(field.getName());


            try {
                setFieldValue(fieldValueFromRequest, field, object);
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
    }


    public static void setFieldValue(Object fieldValue, Field field, Object model)  {
        try {
            field.setAccessible(true);
            field.set(model,fieldValue);
        }
        catch (IllegalAccessException e) {
        	e.printStackTrace();
        }
    }

    public static Object getObject(Class clazz) {
        Object o = null;
        try {
            o = clazz.newInstance();
        } catch (InstantiationException e) {
        	e.printStackTrace();        } 
        catch (IllegalAccessException e) {
        		e.printStackTrace();        }
        return o;
    }

    public static void main(String[] args) {
       
    }

}
