package org.cs304.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BackendApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter("app.pid"));
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
    }

}
