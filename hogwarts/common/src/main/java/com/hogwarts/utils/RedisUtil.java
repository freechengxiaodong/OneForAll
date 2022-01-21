//package com.hogwarts.utils;
//
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStringCommands;
//import org.springframework.data.redis.connection.ReturnType;
//import org.springframework.data.redis.core.RedisConnectionUtils;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.types.Expiration;
//
//import java.nio.charset.Charset;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
///**
// * Redis工具类，使用之前请确保RedisTemplate成功注入
// *
// * @author lxq
// */
//
//public class RedisUtil {
//
//    /**
//     * 解锁脚本，原子操作
//     */
//    private static final String unlockScript =
//            "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n"
//                    + "then\n"
//                    + "    return redis.call(\"del\",KEYS[1])\n"
//                    + "else\n"
//                    + "    return 0\n"
//                    + "end";
//
//    private RedisUtil() {
//    }
//
//    @SuppressWarnings("unchecked")
//    private static RedisTemplate<String, Object> redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
//
//    /**
//     * 设置有效时间
//     * 单位默认秒
//     *
//     * @param key     Redis键
//     * @param timeout 超时时间
//     * @return true=设置成功；false=设置失败
//     */
//    public static boolean expire(final String key, final long timeout) {
//
//        return expire(key, timeout, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 设置有效时间
//     *
//     * @param key     Redis键
//     * @param timeout 超时时间
//     * @param unit    时间单位
//     * @return true=设置成功；false=设置失败
//     */
//    public static boolean expire(final String key, final long timeout, final TimeUnit unit) {
//
//        Boolean ret = redisTemplate.expire(key, timeout, unit);
//        return ret != null && ret;
//    }
//
//    /**
//     * 删除单个key
//     *
//     * @param key 键
//     * @return true=删除成功；false=删除失败
//     */
//    public static boolean del(final String key) {
//
//        Boolean ret = redisTemplate.delete(key);
//        return ret != null && ret;
//    }
//
//    /**
//     * 删除多个key
//     *
//     * @param keys 键集合
//     * @return 成功删除的个数
//     */
//    public static long del(final Collection<String> keys) {
//
//        Long ret = redisTemplate.delete(keys);
//        return ret == null ? 0 : ret;
//    }
//
//    /**
//     * 存入普通对象
//     *
//     * @param key   Redis键
//     * @param value 值
//     */
//    public static void set(final String key, final Object value) {
//
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    /*******************存储普通对象相关操作*********************/
//
//    /**
//     * 存入普通对象
//     *
//     * @param key     键
//     * @param value   值
//     * @param timeout 有效期，单位秒
//     */
//    public static void set(final String key, final Object value, final long timeout) {
//
//        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 获取普通对象
//     *
//     * @param key 键
//     * @return 对象
//     */
//    public static Object get(final String key) {
//
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    /*******************存储Hash相关操作*********************/
//
//    /**
//     * 往Hash中存入数据
//     *
//     * @param key   Redis键
//     * @param field Hash field键
//     * @param value 值
//     */
//    public static void hPut(final String key, final String field, final Object value) {
//
//        redisTemplate.opsForHash().put(key, field, value);
//    }
//
//    /**
//     * 往Hash中存入多个数据
//     *
//     * @param key      Redis键
//     * @param fieldMap Hash键值对
//     */
//    public static void hPutAll(final String key, final Map<String, Object> fieldMap) {
//
//        redisTemplate.opsForHash().putAll(key, fieldMap);
//    }
//
//    /**
//     * 获取Hash中的数据
//     *
//     * @param key   Redis键
//     * @param field Hash field键
//     * @return Hash中的对象
//     */
//    public static Object hGet(final String key, final String field) {
//
//        return redisTemplate.opsForHash().get(key, field);
//    }
//
//    /**
//     * 获取多个Hash中的数据
//     *
//     * @param key    Redis键
//     * @param fields Hash field键集合
//     * @return Hash对象集合
//     */
//    public static List<Object> hMultiGet(final String key, final Collection<Object> fields) {
//
//        return redisTemplate.opsForHash().multiGet(key, fields);
//    }
//
//    /*******************存储Set相关操作*********************/
//
//    /**
//     * 往Set中存入数据
//     *
//     * @param key    Redis键
//     * @param values 值
//     * @return 存入的个数
//     */
//    public static long sSet(final String key, final Object... values) {
//        Long count = redisTemplate.opsForSet().add(key, values);
//        return count == null ? 0 : count;
//    }
//
//    /**
//     * 删除Set中的数据
//     *
//     * @param key    Redis键
//     * @param values 值
//     * @return 移除的个数
//     */
//    public static long sDel(final String key, final Object... values) {
//        Long count = redisTemplate.opsForSet().remove(key, values);
//        return count == null ? 0 : count;
//    }
//
//    /*******************存储List相关操作*********************/
//
//    /**
//     * 往List左侧中存入数据
//     *
//     * @param key   Redis键
//     * @param value 数据
//     * @return 存入的个数
//     */
//    public static long lPush(final String key, final Object value) {
//        Long count = redisTemplate.opsForList().leftPush(key, value);
//        return count == null ? 0 : count;
//    }
//
//    /**
//     * 往List右侧中存入数据
//     *
//     * @param key   Redis键
//     * @param value 数据
//     * @return 存入的个数
//     */
//    public static long rPush(final String key, final Object value) {
//        Long count = redisTemplate.opsForList().rightPush(key, value);
//        return count == null ? 0 : count;
//    }
//
//    /**
//     * 往List中左侧存入多个数据
//     *
//     * @param key    Redis键
//     * @param values 多个数据
//     * @return 存入的个数
//     */
//    public static long lPushAll(final String key, final Collection<Object> values) {
//        Long count = redisTemplate.opsForList().leftPushAll(key, values);
//        return count == null ? 0 : count;
//    }
//
//    /**
//     * 往List中左侧存入多个数据
//     *
//     * @param key    Redis键
//     * @param values 多个数据
//     * @return 存入的个数
//     */
//    public static long lPushAll(final String key, final Object... values) {
//        Long count = redisTemplate.opsForList().leftPushAll(key, values);
//        return count == null ? 0 : count;
//    }
//
//    /**
//     * 往List中右侧存入多个数据
//     *
//     * @param key    Redis键
//     * @param values 多个数据
//     * @return 存入的个数
//     */
//    public static long rPushAll(final String key, final Collection<Object> values) {
//        Long count = redisTemplate.opsForList().rightPushAll(key, values);
//        return count == null ? 0 : count;
//    }
//
//    /**
//     * 往List中右侧存入多个数据
//     *
//     * @param key    Redis键
//     * @param values 多个数据
//     * @return 存入的个数
//     */
//    public static long rPushAll(final String key, final Object... values) {
//        Long count = redisTemplate.opsForList().rightPushAll(key, values);
//        return count == null ? 0 : count;
//    }
//
//    /**
//     * 从List中获取begin到end之间的元素
//     *
//     * @param key   Redis键
//     * @param start 开始位置
//     * @param end   结束位置（start=0，end=-1表示获取全部元素）
//     * @return List对象
//     */
//    public static List<Object> listGetRange(final String key, final int start, final int end) {
//        return redisTemplate.opsForList().range(key, start, end);
//    }
//
//    /**
//     * 从List左侧弹出数据
//     *
//     * @param key Redis键
//     * @return 对象
//     */
//    public static Object listGetL(final String key) {
//        return redisTemplate.opsForList().leftPop(key);
//    }
//
//    /**
//     * 从List右侧弹出数据
//     *
//     * @param key Redis键
//     * @return 对象
//     */
//    public static Object listGetR(final String key) {
//        return redisTemplate.opsForList().rightPop(key);
//    }
//
//    /**
//     * 加锁，有阻塞
//     *
//     * @param name
//     * @param expire
//     * @param timeout
//     * @return
//     */
//    public static String lock(String name, long expire, long timeout) {
//        String token;
//        long startTime = System.currentTimeMillis();
//        do {
//            token = tryLock(name, expire);
//            if (token == null) {
//                if ((System.currentTimeMillis() - startTime) > (timeout - 50))
//                    break;
//                try {
//                    Thread.sleep(50); //try 50 per sec
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//        } while (token == null);
//
//        return token;
//    }
//
//    /**
//     * 加锁，无阻塞
//     *
//     * @param name
//     * @param expire
//     * @return
//     */
//    public static String tryLock(String name, long expire) {
//        String token = UUID.randomUUID().toString();
//        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
//        RedisConnection conn = factory.getConnection();
//        try {
//            Boolean result = conn.set(name.getBytes(Charset.forName("UTF-8")), token.getBytes(Charset.forName("UTF-8")),
//                    Expiration.from(expire, TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
//            if (result != null && result)
//                return token;
//        } finally {
//            RedisConnectionUtils.releaseConnection(conn, factory, false);
//        }
//
//        return null;
//    }
//
//    /**
//     * 解锁
//     *
//     * @param name
//     * @param token
//     * @return
//     */
//    public static boolean unlock(String name, String token) {
//        byte[][] keysAndArgs = new byte[2][];
//        keysAndArgs[0] = name.getBytes(Charset.forName("UTF-8"));
//        keysAndArgs[1] = token.getBytes(Charset.forName("UTF-8"));
//        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
//        RedisConnection conn = factory.getConnection();
//        try {
//            Long result = (Long) conn.scriptingCommands().eval(unlockScript.getBytes(Charset.forName("UTF-8")), ReturnType.INTEGER, 1, keysAndArgs);
//            if (result != null && result > 0)
//                return true;
//        } finally {
//            RedisConnectionUtils.releaseConnection(conn, factory, false);
//        }
//
//        return false;
//    }
//}