package xyz.ersut;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//表示是个启动类
@SpringBootApplication
//开启AOP
@EnableAspectJAutoProxy(exposeProxy = true)
//激活ConfigurationProperties注解
//@EnableConfigurationProperties({CommonConstants.class})
//开启缓存功能 @Cacheable
@EnableCaching
// 开启定时任务
@EnableScheduling
//mapper.xml扫描
@MapperScan({"xyz.ersut.*.mapper"})
//开启注解式事务
@EnableTransactionManagement
/**
 * @author 王二飞
 */
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        //生成pid文件
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }
}