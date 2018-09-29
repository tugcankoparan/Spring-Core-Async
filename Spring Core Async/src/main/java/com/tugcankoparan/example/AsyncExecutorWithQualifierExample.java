package com.tugcankoparan.example;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncExecutorWithQualifierExample {

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                  new AnnotationConfigApplicationContext(
                            MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling async method from thread: %s%n",
                          Thread.currentThread().getName());
        bean.runTask();

        ThreadPoolTaskExecutor exec = context.getBean(ThreadPoolTaskExecutor.class);
        exec.getThreadPoolExecutor().shutdown();
    }

    @EnableAsync
    @Configuration
    public static class MyConfig {
        @Bean
        public MyBean myBean () {
            return new MyBean();
        }

        @Bean
        @Qualifier("myExecutor1")
        public TaskExecutor taskExecutor2 () {
            return new ConcurrentTaskExecutor(
                      Executors.newFixedThreadPool(3));
        }

        @Bean
        @Qualifier("myExecutor2")
        public TaskExecutor taskExecutor () {
            return new ThreadPoolTaskExecutor();
        }
    }

    private static class MyBean {

        @Async("myExecutor2")
        public void runTask () {
            System.out.printf("Running task  thread: %s%n",
                              Thread.currentThread().getName());
        }
    }
}