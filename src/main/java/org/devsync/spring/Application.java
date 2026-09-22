package org.devsync.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
public class Application {

    static void main(String[] args) {

        System.out.println(TimeZone.getDefault().getID());
        SpringApplication.run(Application.class, args);
    }

}
