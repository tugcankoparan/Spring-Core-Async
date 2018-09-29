package com.tugcankoparan.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

public class AsyncArgAndReturnValueExample {

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                  new AnnotationConfigApplicationContext(
                            MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n",
                          Thread.currentThread().getName());
        String s = bean.runTask("from main");
        System.out.println("call MyBean#runTask() returned");
        System.out.println("returned value: " + s);
    }

    @EnableAsync
    @Configuration
    public static class MyConfig {
        @Bean
        public MyBean myBean () {
            return new MyBean();
        }
    }

    private static class MyBean {

        @Async
        public String runTask (String message) {
            System.out.printf("Running task  thread: %s%n",
                              Thread.currentThread().getName());
            System.out.printf("message: %s%n", message);
            System.out.println("task ends");
            return "return value";
        }
    }
}