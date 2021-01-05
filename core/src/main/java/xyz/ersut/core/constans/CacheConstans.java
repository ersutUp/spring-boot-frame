package xyz.ersut.core.constans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 王二飞
 */
@Component
public class CacheConstans {;

    /**前缀*/
    @Value("${prefix:project}")
    public String prefix;

    /**一天的秒*/
    public static final long ONEDAYMS = 86400;

    /** 手机验证码key的前缀 */
    public static final String CAPTCHA = "captcha";
    /** 手机验证码的过期时间 */
    public static final long CAPTCHATIME = 300;


}
