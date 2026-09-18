package org.devsync.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.TimeZone;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Application {

    static void main(String[] args) {

        System.out.println(TimeZone.getDefault().getID());
        SpringApplication.run(Application.class, args);
    }

}
