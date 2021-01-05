package xyz.ersut.config;

import xyz.ersut.common.intercepter.ApiVerifyInterceptor;
import xyz.ersut.core.util.verify.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * 拦截器配置
 * @author 王二飞
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private ApiVerifyInterceptor apiVerifyFilter;

    @Value("${spring.profiles.active}")
    private String[] active;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        if(ObjectUtils.isNotNull(active) && Arrays.asList(active).contains("prod")){
            //接口验证
            registry.addInterceptor(apiVerifyFilter)
                    .addPathPatterns("/api/**/*");
        }
    }
}
