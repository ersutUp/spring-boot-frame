package xyz.ersut.core.base;

import xyz.ersut.core.constans.ConfigConstans;
import xyz.ersut.core.server.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * base总类（根类）
 * @author 王二飞
 */
@Component
public class Base {

    @Autowired
    protected ConfigConstans configConstans;

    @Autowired(required = false)
    @Qualifier("redisUtil")
    protected Cache redis;

}
