package com.security.utils;

import java.util.UUID;

/**
 * @author
 * @date 2019/10/9
 */
public class IdGeneratorUtil {

    /**
     * 生成uuid 作为id 主键使用
     * @return 主键id
     */
    public static String uuid() {
      return UUID.randomUUID().toString().replace("-","");
    }
}
