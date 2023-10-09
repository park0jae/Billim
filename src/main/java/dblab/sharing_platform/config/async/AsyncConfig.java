package dblab.sharing_platform.config.async;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private final Integer PROCESSORS = Runtime.getRuntime().availableProcessors();
    private final Integer MAX_POOL_SIZE = PROCESSORS * 2;
    private final Integer QUEUE_CAPACITY = 50;
    private final Integer KEEP_ALIVE_SECONDS = 60;
    public static final String ASYNC_EXECUTOR_NAME = "AsyncExecutor";

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(PROCESSORS);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        executor.setThreadNamePrefix(ASYNC_EXECUTOR_NAME);
        executor.initialize();
        return executor;
    }
}
