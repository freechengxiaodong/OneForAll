package com.tools.utils;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class JRedisUtil {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取分布式锁
     *
     * @param key
     * @param val
     * @return
     */
    public boolean setNx(String key, String val) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            log.info("jedis getResource end{}", jedis);
            if (jedis == null) {
                return false;
            }

            //设置参数
            SetParams setParams = SetParams.setParams();
            //NX：是否存在key，存在就不set成功
            setParams.nx();
            //key过期时间单位设置为毫秒（EX：单位秒）
            setParams.px(1000 * 60);

            log.info("KEY {}, VALUE {}", key, val);
            return jedis.set(key, val, setParams).equalsIgnoreCase("ok");
            //return jedis.set(key, val, "NX", "PX", 1000 * 60).equalsIgnoreCase("ok");
        } catch (Exception ex) {
            log.info("Exception {}", ex.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param key
     * @param val
     * @return
     */
    public int delNx(String key, String val) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis == null) {
                return 0;
            }

            //if redis.call('get','orderkey')=='1111' then return redis.call('del','orderkey') else return 0 end
            StringBuilder sbScript = new StringBuilder();
            sbScript.append("if redis.call('get','").append(key).append("')").append("=='").append(val).append("'").
                    append(" then ").
                    append("    return redis.call('del','").append(key).append("')").
                    append(" else ").
                    append("    return 0").
                    append(" end");

            return Integer.valueOf(jedis.eval(sbScript.toString()).toString());
        } catch (Exception ex) {

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return 0;
    }

    public void set(String key, String value) {

        @Cleanup Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
    }

    public String get(String key) {

        @Cleanup Jedis jedis = jedisPool.getResource();
        return jedis.get(key);
    }

    /**
     * String数据类型
     */
    public void string() {
        Jedis jedis = jedisPool.getResource();

        // 设置一个字符串, 如果key存在, 覆盖value, 如果key不存在, 新创建一个key value
        // Redis命令: set key value
        jedis.set("myStr", "111");
        System.out.println("set: " + jedis.get("myStr"));

        // 设置一个字符串, 当key存在时, 设置失败, 不存在时, 设置成功
        // Redis命令: set key value nx
        jedis.setnx("myStr", "222");
        System.out.println("setnx: " + jedis.get("myStr"));

        // 设置一个字符串, 并设置过期时间, 时间单位秒
        // Redis命令: set key value [EX seconds]
        jedis.setex("myStr", 60, "333");
        System.out.println("setex: " + jedis.get("myStr") + ", 还有" + jedis.ttl("myStr") + "秒过期!");

        // 设置一个字符串, 指定可选的参数
        // Redis命令: set key value [EX seconds] [PX milliseconds] [NX|XX]
        SetParams params = SetParams.setParams();
        params.ex(600); // 秒
        params.xx(); // 强制覆盖
        jedis.set("myStr", "444", params);
        System.out.println("set SetParams: " + jedis.get("myStr") + ", 还有" + jedis.ttl("myStr") + "秒过期!");

        // 一次性设置多个字符串
        // Redis命令: mset key value [key value ...]
        //            mget key [key ...]
        jedis.mset("key1", "value1", "key2", "value2");
        System.out.println("mset: " + jedis.mget("key1", "key2"));

        // 查询key是否存在
        // Redis命令: exists key [key ...]
        boolean isExists = jedis.exists("myStr");

        // 给字符串重命名, 当key不存在时, 会报错
        // Redis命令: rename key newkey
        //            renamenx key newkey
        jedis.rename("myStr", "newStr");
        jedis.renamenx("newStr", "aaabbb");

        // 设置一个number的元素, 然后再进行累计1
        jedis.set("myNum", "66");
        jedis.incr("myNum");
        System.out.println("incr: " + jedis.get("myNum"));
    }

    /**
     * ZSet有序列表数据类型
     */
    public void zset() {
        Jedis jedis = jedisPool.getResource();

        // 删除指定的key, 相当于把整个key在Redis中删除了
        // Redis命令: del key [key ...]
        jedis.del("myZSet");

        // 设置一个ZSet, 往列表中添加元素, 该列表是一个有序的列表
        // Redis命令: zadd key [NX|XX] [CH] [INCR] score member [score member ...]
        //            zrange key start stop [WITHSCORES]
        Map<String, Double> zadd = new HashMap<String, Double>(); // Map中的元素是无序的
        zadd.put("zadd1", 100D);
        zadd.put("zadd2", 200D);
        zadd.put("zadd3", 300D);
        zadd.put("zadd4", 100D);
        jedis.zadd("myZSet", zadd);
        System.out.println("zadd: " + jedis.zrange("myZSet", 0, -1));

        // 删除ZSet中某个元素
        // Redis命令: zrem key member [member ...]
        jedis.zrem("myZSet", "zadd3");
        System.out.println("zrem: " + jedis.zrange("myZSet", 0, -1));

        // 倒序展示ZSet中的元素
        // Redis命令: zrevrange key start stop [WITHSCORES]
        System.out.println("zrevrange: " + jedis.zrevrange("myZSet", 0, -1));
        System.out.println("zrevrangeWithScores: " + jedis.zrevrangeWithScores("myZSet", 0, -1));

        // 返回ZSet中元素个数
        // Redis命令: zcard key
        Long zcard = jedis.zcard("myZSet");
        System.out.println("zcard: " + zcard);
    }

    /**
     * Set无序列表数据类型
     */
    public void set() {
        Jedis jedis = jedisPool.getResource();

        // 删除指定的key, 相当于把整个key在Redis中删除了
        // Redis命令: del key [key ...]
        jedis.del("mySet");

        // 设置一个Set, 往列表中添加元素, 该列表是一个无序的列表
        // Redis命令: sadd key member [member ...]
        //            smembers key
        jedis.sadd("mySet", "111", "222", "333");
        System.out.println("sadd: " + jedis.smembers("mySet"));

        // 删除Set中某个元素
        // Redis命令: srem key member [member ...]
        jedis.srem("mySet", "222");
        System.out.println("srem: " + jedis.smembers("mySet"));

        // 弹出Set中尾部元素, 相当于删除尾部元素
        // Redis命令: srem key member [member ...]
        jedis.spop("mySet");
        System.out.println("spop: " + jedis.smembers("mySet"));

        // 返回Set中元素个数
        // Redis命令: scard key
        Long scard = jedis.scard("mySet");
        System.out.println("scard: " + scard);
    }

    /**
     * Hash数据类型
     */
    public void hash() {
        Jedis jedis = jedisPool.getResource();

        // 删除指定的key, 相当于把整个key在Redis中删除了
        // Redis命令: del key [key ...]
        jedis.del("myHash");

        // 设置一个Hash
        // Redis命令: hset key field value
        //            hget key field
        jedis.hset("myHash", "key1", "value1");
        System.out.println("hset: " + jedis.hget("myHash", "key1"));

        // 该方式只能添加一个key value, 当多个key value时, 会报错。原因是hset方法实现问题, hset命令不支持一次性插入多个key value
        final Map<String, String> hset = new HashMap<String, String>();
        hset.put("k1", "v1");
        jedis.hset("myHash", hset);
        System.out.println("hgetAll: " + jedis.hgetAll("myHash"));

        // 一次性设置多个Hash, 当key存在时, 覆盖value
        // Redis命令: hmset key field value [field value ...]
        //            hmget key field [field ...]
        final Map<String, String> hmset = new HashMap<String, String>();
        hmset.put("k1", "vvv111");
        hmset.put("k2", "v2");
        hmset.put("k3", "v3");
        jedis.hmset("myHash", hmset);
        System.out.println("hmset: " + jedis.hgetAll("myHash"));

        // 判断key中, 某一个字段是否存在
        // Redis命令: hexists key field
        boolean isHexists = jedis.hexists("myHash", "k1");
        System.out.println("hexists: " + isHexists);

        // 列表长度
        // Redis命令: hlen key
        Long hlen = jedis.hlen("myHash");
        System.out.println("hlen: " + hlen);

        // 删除指定列表下标的value, 如value不一致, 删除失败
        // Redis命令: hdel key field [field ...]
        jedis.hdel("myHash", "k1", "k3");
        System.out.println("hdel: " + jedis.hgetAll("myHash"));

        // 获取Hash中所有的key
        // Redis命令: hkeys key
        Set<String> hkeys = jedis.hkeys("myHash");
        System.out.println("hkeys: " + hkeys);

        // 获取Hash中所有的value
        // Redis命令: hvals key
        List<String> hvals = jedis.hvals("myHash");
        System.out.println("hvals: " + hvals);
    }

    /**
     * List链表数据类型
     */
    public void list() {
        Jedis jedis = jedisPool.getResource();

        // 删除指定的key, 相当于把整个key在Redis中删除了
        // Redis命令: del key [key ...]
        jedis.del("myList");

        // 设置一个列表, 往列表头部添加元素
        // Redis命令: lpush key value [value ...]
        jedis.lpush("myList", "111", "222", "333");
        System.out.println("lpush: " + jedis.lrange("myList", 0, -1));

        // 设置一个列表, 往列表尾部添加元素
        // Redis命令: rpush key value [value ...]
        jedis.rpush("myList", "AAA", "BBB", "CCC");
        System.out.println("rpush: " + jedis.lrange("myList", 0, -1));

        // 列表长度
        // Redis命令: llen key
        jedis.llen("myList");

        // 指定获取列表区间范围的元素, 其它元素都抛弃掉
        // Redis命令: ltrim key start stop
        jedis.ltrim("myList", 3, 5);
        System.out.println("ltrim: " + jedis.lrange("myList", 0, -1));

        // 重新设置, 指定列表下标的value
        // Redis命令: lset key index value
        jedis.lset("myList", 1, "DDD");
        System.out.println("lset: " + jedis.lrange("myList", 0, -1));

        // 删除指定列表下标的value, 如value不一致, 删除失败
        // Redis命令: lrem key count value
        jedis.lrem("myList", 1, "DDD");
        System.out.println("lrem: " + jedis.lrange("myList", 0, -1));

        // 从列表头部弹出元素
        // Redis命令: lpop key
        jedis.lpop("myList");
        System.out.println("lpop: " + jedis.lrange("myList", 0, -1));

        // 从列表尾部弹出元素
        // Redis命令: rpop key
        jedis.rpop("myList");
        System.out.println("rpop: " + jedis.lrange("myList", 0, -1));
    }
}
