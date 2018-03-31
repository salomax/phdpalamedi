package phd.palamedi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import phd.palamedi.model.Publisher;
import phd.palamedi.model.ThreadPoolMonitor;
import phd.palamedi.service.PublisherService;

import java.util.List;

/**
 * Created by marcos.salomao on 24/3/18.
 */
@RestController
@RequestMapping("/task")
public class TaskExecutorController {

    @Autowired
    private TaskExecutor taskExecutor;

    @RequestMapping(value = "live", method = RequestMethod.GET)
    public ThreadPoolMonitor get() {

        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor();

        if (taskExecutor instanceof ThreadPoolTaskExecutor) {

            ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) taskExecutor;

            threadPoolMonitor.setPoolSize(threadPoolTaskExecutor.getPoolSize());
            threadPoolMonitor.setActiveCount(threadPoolTaskExecutor.getActiveCount());
            threadPoolMonitor.setMaxPoolSize(threadPoolTaskExecutor.getCorePoolSize());
            threadPoolMonitor.setQueueSize(threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().size());

            return threadPoolMonitor;

        }

        throw new IllegalStateException("Executor tasks is not a thread pool object");

    }

}
