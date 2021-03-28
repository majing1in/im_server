package com.xiaoma.im.utils;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 现在一般使用这个RedisTemplate，会以二进制形式存储数据到redis中
 */
@Component
public final class RedisTemplateUtils {

    /**
     * 注意此处一定要使用@Resource注解，使用@Autowired获取不到这个RedisTemplate的
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置String类型key的对象
     *
     * @param key
     * @param object
     */
    public void setObject(String key, Object object) {
        redisTemplate.opsForValue().set(key, object);
    }

    /**
     * 设置String类型key的对象，带有过期时间
     *
     * @param key
     * @param object
     * @param timeOut
     */
    public void setObject(String key, Object object, Long timeOut) {
        redisTemplate.opsForValue().set(key, object);
        if (timeOut != null) {
            redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取指定key的对象数据
     *
     * @param key
     * @return
     */
    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 存储数据或修改数据
     *
     * @param modelMap
     * @param mapName
     */
    public void setKey(String mapName, Map<String, Object> modelMap) {
        HashOperations<String, String, Object> hps = redisTemplate.opsForHash();
        hps.putAll(mapName, modelMap);
    }

    /**
     * 获取数据Map
     *
     * @param mapName
     * @return
     */
    public Map<String, Object> getMapValue(String mapName) {
        HashOperations<String, String, Object> hps = this.redisTemplate.opsForHash();
        return hps.entries(mapName);

    }

    /**
     * 获取数据value
     *
     * @param mapName
     * @param hashKey
     * @return
     */
    public Object getValue(String mapName, String hashKey) {
        HashOperations<String, String, Object> hps = this.redisTemplate.opsForHash();
        return hps.get(mapName, hashKey);

    }

    /**
     * 批量删除缓存数据
     *
     * @param keys
     */
    public void deleteData(List<String> keys) {
        // 执行批量删除操作时先序列化template
        redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
        redisTemplate.delete(keys);
    }

    /**
     * 存放list集合数据进redis
     *
     * @param key  list数据对应的key
     * @param list 需要存放的list数据
     */
    public void setList(String key, List<Object> list) {
        redisTemplate.opsForList().leftPushAll(key, list);
    }

    public <T> List<T> getList(String key) {
        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) redisTemplate.opsForList().range(key, 0, 10);
        return list;
    }

    /**
     * 往List中存入数据
     *
     * @param key   Redis键
     * @param value 数据
     * @return 存入的个数
     */
    public long lPush(final String key, final Object value) {
        Long count = redisTemplate.opsForList().leftPush(key, value);
        return count == null ? 0 : count;
    }

    /**
     * 从右边向列表中添加值
     *
     * @param key key
     * @param value 要添加的值
     * @return 存入的个数
     */
    public long rPush(String key, final Object value) {
        Long count = redisTemplate.opsForList().rightPush(key, value);
        return count == null ? 0 : count;
    }


    /**
     * 往List中存入多个数据
     *
     * @param key    Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public long lPushAll(final String key, final Collection<Object> values) {
        Long count = redisTemplate.opsForList().leftPush(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key    Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public long rPushAll(final String key, final Collection<Object> values) {
        Long count = redisTemplate.opsForList().rightPush(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 往List中存入多个数据
     *
     * @param key    Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public long lPushAll(final String key, final Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        return count == null ? 0 : count;
    }

    public boolean lPushIfPresent(final String key, Object value) {
        Long count = redisTemplate.opsForList().leftPushIfPresent(key, value);
        return count != null;
    }

    public boolean rPushIfPresent(final String key, Object value) {
        Long count = redisTemplate.opsForList().rightPushIfPresent(key, value);
        return count != null;
    }

    public Object lPop(final String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public boolean RPop(final String key) {
        Object o = redisTemplate.opsForList().rightPop(key);
        return o != null;
    }

    /**
     * 从List中获取begin到end之间的元素
     *
     * @param key   Redis键
     * @param start 开始位置
     * @param end   结束位置（start=0，end=-1表示获取全部元素）
     * @return List对象
     */
    public List<Object> lGet(final String key, final int start, final int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    //****************** map ******************//
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Object hGet(String key, String value) {
        return redisTemplate.opsForHash().get(key, value);
    }

    public void hPut(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public boolean hPutIfAbsent(String key, Object hashKey, Object value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    public boolean hDelete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields) > 0 ? true : false;
    }

}