package xyz.ersut.core.server.cache;

import java.util.List;

public interface Cache {
    /**
     * 指定缓存失效时间
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time);

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key);

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key);

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    public void remove(String... key);

    /**
     * 通用key获取
     * @param key 键
     * @param clazz 获取的类型
     * @return
     */
    public <T> T get(String key, Class<T> clazz);

    /**
     * 通用key获取
     * @param key 键
     * @return
     */
    public Object get(String key);

    /**
     * 通用存储缓存（可设置过期时间）
     * @param key 键
     * @param value 值
     * @param time 存储的时间(秒)
     * @return
     */
    public boolean set(String key, Object value, long time);

    /**
     * 通用存储缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean set(String key, Object value);


    /**
     * 模糊查询缓存
     *
     * @param  key
     *          键的前缀
     * @return 值
     */
    public <T> List<T> getKeyByPrefix(String key);

    /**
     * 删除某前缀的缓存
     * @param keyPrefix
     *          前缀
     */
    void removeByPrefix(String keyPrefix);

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end);
    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key);
    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value);
    /**
     * list 从集合左侧放入一个
     * @param key
     * @param value
     */
    void lpush(String key, Object value);
    /**
     * list 从集合左侧取出一个
     *  @param key   键
     */
    public Object lpop(String key);
    /**
     * list 从集合右侧取出一个
     * @param key
     */
    Object rpop(String key);
    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    long lRemove(String key, long count, Object value);


    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values);

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values);

}
