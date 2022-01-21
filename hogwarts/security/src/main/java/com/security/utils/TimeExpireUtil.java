package com.security.utils;

import java.text.ParseException;
import java.util.Date;

/**
 * @author
 * @date 2019/10/10
 */
public class TimeExpireUtil {

    /**
     * 判断日期是否过期
     * @param day 时间 yyyyMMdd
     * @return true 过期  false 未过期
     */
    public static boolean isExpire(String day) {
        try {
            Date expireDate = DateFormatUtil.pareDate("yyyyMMdd", day);
            // 如果过期了 直接返回空
            if (expireDate.before(new Date())) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
       return false;
    }
}
