package xyz.ersut.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 关于异步事物的说明：
 *  方法A，使用了@Async/@Transactional来标注，但是无法产生事务控制的目的。
 *  方法B，使用了@Async来标注，  B中调用了C、D，C/D分别使用@Transactional做了标注，则可实现事务控制的目的。
 *  注：本框架已集成全局事务
 *  在本框架中异步内调用service层则事务自动生效
 *
 * @author 王二飞
 */
@Configuration
//启动异步
@EnableAsync
public class ThreadPoolConfig {

    @Bean("projectExecutor")
    public Executor projectExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(10);
        //线程池最大
        executor.setMaxPoolSize(20);
        //缓冲的线程
        executor.setQueueCapacity(200);
        //当超过了核心线程出之外的线程在空闲时间（S）到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        //线程池的前缀
        executor.setThreadNamePrefix("projectExecutor-");

        //关闭程序时需等待线程池内任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //设置线程在关闭程序时应阻塞的最大秒数，以便在容器的其余部分继续关闭之前等待剩余任务完成其执行
        executor.setAwaitTerminationSeconds(60);
        /*
        参考链接：https://segmentfault.com/a/1190000009111732
        可用值：
            ThreadPoolExecutor.AbortPolicy 这个是默认使用的拒绝策略,如果有要执行的任务队列已满,且还有任务提交,则直接抛出异常信息

            ThreadPoolExecutor.DiscardPolicy 这个是忽略策略,如果有要执行的任务队列已满,且还有任务提交,则直接忽略掉这个任务,即不抛出异常也不做任何处理.

            ThreadPoolExecutor.DiscardOldestPolicy 忽略最早提交的任务.如果有要执行的任务队列已满,此时若还有任务提交且线程池还没有停止,则把队列中最早提交的任务抛弃掉,然后把当前任务加入队列中.

            ThreadPoolExecutor.CallerRunsPolicy 这个是来着不拒策略.如果有要执行的任务队列已满,此时若还有任务提交且线程池还没有停止,则直接运行任务的run方法.
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
