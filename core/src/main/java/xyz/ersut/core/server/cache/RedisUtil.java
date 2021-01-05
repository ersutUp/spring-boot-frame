package xyz.ersut.core.server.cache;

import xyz.ersut.core.base.Base;
import xyz.ersut.core.util.verify.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component("redisUtil")
public class RedisUtil extends Base implements Cache  {
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    // =============================common============================
    /**
     * 指定缓存失效时间
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    @Override
    public boolean expire(String key, long time) {
        key = configConstans.prefix+key;
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    @Override
    public long getExpire(String key) {
        key = configConstans.prefix+key;
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    @Override
    public boolean hasKey(String key) {
        key = configConstans.prefix+key;
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @Override
    @SuppressWarnings("unchecked")
    public void remove(String... key) {
        if (key != null && key.length > 0) {
            for (int i = 0; i < key.length;i++) {
                String tKey = configConstans.prefix+key[i];
                key[i] = tKey;
            }
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 通用获取
     * @param key 键
     * @param clazz 获取的类型
     * @return
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        if(clazz.isInterface()){
            throw new RuntimeException("'clazz' param cannot be interface");
        }
        try {
            if(Map.class.isInstance(clazz.newInstance())){
                return (T)hmget(key);
            } else if(List.class.isInstance(clazz.newInstance())){
                return (T)lGet(key,0,-1);
            } else if(Set.class.isInstance(clazz.newInstance())){
                return (T)sGet(key);
            } else {
                return (T)baGet(key);
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 通用key获取
     * @param key 键
     * @return
     */
    @Override
    public Object get(String key) {
        return baGet(key);
    }
    /**
     * 通用存储缓存（可设置过期时间）
     * @param key 键
     * @param value 值
     * @param time 存储的时间
     * @return
     */
    @Override
    public boolean set(String key, Object value, long time) {
//        remove(key);
        if(ObjectUtils.isNull(value)){
            return false;
        }
        if(Map.class.isInstance(value)){
            return hmset(key,(Map)value,time);
        } else if(List.class.isInstance(value)){
            return lSet(key,(List) value,time);
        } else if(Set.class.isInstance(value)){
            return sSetAndTime(key,time,((Set)value).toArray()) > 0;
        } else {
            return baSet(key,value,time);
        }
    }
    /**
     * 通用存储缓存
     * @param key 键
     * @param value 值
     * @return
     */
    @Override
    public boolean set(String key, Object value) {
//        remove(key);
        if(ObjectUtils.isNull(value)){
            return false;
        }
        if(Map.class.isInstance(value)){
            return hmset(key,(Map)value);
        } else if(List.class.isInstance(value)){
            return lSet(key,(List) value);
        } else if(Set.class.isInstance(value)){
            return sSet(key, ((Set)value).toArray()) > 0;
        } else {
            return baSet(key,value);
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object baGet(String key) {
        key = configConstans.prefix+key;
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 模糊查询缓存
     *
     * @param  key
     *          键的前缀
     * @return 值
     */
    @Override
    public <T> List<T> getKeyByPrefix(String key) {
        key = configConstans.prefix+key;
        Set<String> keys = redisTemplate.keys(key + "*");

        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<T>();
        }

        List<T> resourceCacheList = new ArrayList<T>();
        for (String accurateKey : keys) {
            T cacheValue =  (T)redisTemplate.opsForValue().get(accurateKey);
            resourceCacheList.add(cacheValue);
        }

        return resourceCacheList;
    }

    @Override
    public void removeByPrefix(String keyPrefix) {
        keyPrefix = configConstans.prefix+keyPrefix;
        Set<String> keys = redisTemplate.keys(keyPrefix + "*");

        if(keys.size()==0){
            return;
        }
        String[] keyArr = keys.toArray(new String[keys.size()]);
        if (keyArr.length == 1) {
            redisTemplate.delete(keyArr[0]);
        } else {
            redisTemplate.delete(CollectionUtils.arrayToList(keyArr));
        }
    }


    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean baSet(String key, Object value) {
        key = configConstans.prefix+key;
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean baSet(String key, Object value, long time) {
        if(ObjectUtils.isNull(value)){
            return false;
        }
        try {
            if (time > 0) {
                key = configConstans.prefix+key;
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                baSet(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        key = configConstans.prefix+key;
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        key = configConstans.prefix+key;
        return redisTemplate.opsForValue().increment(key, -delta);
    }
    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        key = configConstans.prefix+key;
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        key = configConstans.prefix+key;
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        key = configConstans.prefix+key;
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        if(ObjectUtils.isNull(map)){
            return false;
        }
        String tkey = configConstans.prefix+key;
        try {
            redisTemplate.opsForHash().putAll(tkey, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        key = configConstans.prefix+key;
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        String tkey = configConstans.prefix+key;
        try {
            redisTemplate.opsForHash().put(tkey, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        key = configConstans.prefix+key;
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        key = configConstans.prefix+key;
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        key = configConstans.prefix+key;
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        key = configConstans.prefix+key;
        return redisTemplate.opsForHash().increment(key, item, -by);
    }
    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        key = configConstans.prefix+key;
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        key = configConstans.prefix+key;
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public long sSet(String key, Object... values) {
        key = configConstans.prefix+key;
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @Override
    public long sSetAndTime(String key, long time, Object... values) {
        if(ObjectUtils.isNull(time)){
            return 0;
        }
        String tkey = configConstans.prefix+key;
        try {
            Long count = redisTemplate.opsForSet().add(tkey, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        key = configConstans.prefix+key;
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        key = configConstans.prefix+key;
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    @Override
    public List<Object> lGet(String key, long start, long end) {
        key = configConstans.prefix+key;
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    @Override
    public long lGetListSize(String key) {
        key = configConstans.prefix+key;
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        key = configConstans.prefix+key;
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @Override
    public boolean lSet(String key, Object value) {
        key = configConstans.prefix+key;
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        String tkey = configConstans.prefix+key;
        try {
            redisTemplate.opsForList().rightPush(tkey, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        if(value.size()==0){
            return false;
        }
        key = configConstans.prefix+key;
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        if(ObjectUtils.isNull(value)){
            return false;
        }
        String tkey = configConstans.prefix+key;
        try {
            redisTemplate.opsForList().rightPushAll(tkey, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        key = configConstans.prefix+key;
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    @Override
    public long lRemove(String key, long count, Object value) {
        key = configConstans.prefix+key;
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * list 从集合左侧放入一个
     *  @param key   键
     * @param value 值
     */
    @Override
    public void lpush(String key, Object value) {
        key = configConstans.prefix+key;
        try {
            Long remove = redisTemplate.opsForList().leftPush(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * list 从集合左侧取出一个
     *  @param key   键
     */
    @Override
    public Object lpop(String key) {
        key = configConstans.prefix+key;
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * list 从集合右侧取出一个
     *
     * @param key   键
     */
    @Override
    public Object rpop(String key) {
        key = configConstans.prefix+key;
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}