package com.hogwarts.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * JSON util class based on Jackson-2.5.2
 *
 * @author Jiangdanfeng
 * @since 2016-07-26
 */
public abstract class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static JsonNode toTree(String json) throws IOException {
        return MAPPER.readTree(json);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String json) throws IOException {
        if (StringUtils.isEmpty(json)) {
            return new HashMap<String, Object>(0);
        }
        return MAPPER.readValue(json, Map.class);
    }

    public static <T> T toBean(String json, Class<T> clazz) throws IOException {
        if (StringUtils.isBlank(json))
            return null;
        return MAPPER.readValue(json, clazz);
    }

    public static <T> T toBean(String json, TypeReference<T> ref) throws IOException {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return MAPPER.readValue(json, ref);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(String json, Class<T> clazz) throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructParametrizedType(List.class, clazz, clazz);
        return (List<T>) MAPPER.readValue(json, javaType);
    }

    public static String toJson(Object object) throws IOException {
        return MAPPER.writeValueAsString(object);
    }

    public static String toJsonWithoutNull(Object object) throws IOException {
        return MAPPER.writeValueAsString(object);
    }

    /**
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> beanToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!key.equals("class")) {
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);
                map.put(key, value);
            }

        }
        return map;
    }

    /**
     * Bean转传成TreeMap
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static TreeMap<String, Object> beanToTreeMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        TreeMap<String, Object> map = new TreeMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!key.equals("class")) {
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, String> beanToStringMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!key.equals("class")) {
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);
                map.put(key, value == null ? null : value.toString());
            }

        }
        return map;
    }

    /**
     * 基于BeanUtils.populate(Object, Map)将Map转换成Bean
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) throws Exception {
        T t = clazz.newInstance();
        BeanUtils.populate(t, map);
        return t;
    }

    /**
     * 基于Jackson将Bean转换成Map, 经历 两步转换：1. 将Object序列化成JSON; 2. 将JSON反序列化成Map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> covertToMap(Object obj) throws Exception {
        return MAPPER.convertValue(obj, HashMap.class);
    }

    /**
     * 基于Jackson将Map转换成Bean, 经历 两步转换：1. 将Map序列化成JSON; 2. 将JSON反序列化成Bean
     *
     * @param map
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T convertMapToBean(Map<String, Object> map, Class<T> clazz) throws Exception {
        return MAPPER.convertValue(map, clazz);
    }
}

