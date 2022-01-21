/**
 * @copyright 2018 sinping.com 北京芯平科技股份有限公司. All rights reserved.
 * 本内容仅限于北京芯平科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的.
 */
package com.security.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @author
 */
@Slf4j
@UtilityClass
public class JsonUtil {

    public String parser(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("Object 转换失败 -- {}", obj.toString());
        }
        return null;
    }

    public Object convert(String json, Class<?> clazz) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            if (json.startsWith(JSON_ARRAY_PREFIX) && json.endsWith(JSON_ARRAY_SUFFIX)) {
                JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
                return mapper.readValue(json, javaType);
            } else {
                return mapper.readValue(json, clazz);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("json 转换失败 -- {}", json);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T copy(T t) {
        try {
            String json = mapper.writeValueAsString(t);
            return (T) mapper.readValue(json, t.getClass());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("json 转换失败。");
        }
        return t;
    }

    private static final ObjectMapper mapper;
    private static final String JSON_ARRAY_PREFIX = "[";
    private static final String JSON_ARRAY_SUFFIX = "]";

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
    }

}
