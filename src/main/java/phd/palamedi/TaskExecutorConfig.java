package phd.palamedi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by marcos.salomao on 24/3/18.
 */
@Configuration
public class TaskExecutorConfig {

    @Value("${phdpalamedi.poolSize}")
    private Integer poolSize;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setThreadNamePrefix("article_thread");
        executor.initialize();
        return executor;
    }

}
