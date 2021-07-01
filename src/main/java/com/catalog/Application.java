package com.catalog;

import com.catalog.consumer.SQSConsumer;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication (exclude = {ContextStackAutoConfiguration.class})
public class Application implements CommandLineRunner {

    @Autowired
    private SQSConsumer consumer;

    @Bean
    Mapper mapper()
    {
        return DozerBeanMapperBuilder.buildDefault();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        consumer.run();
    }
}