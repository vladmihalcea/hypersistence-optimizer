package io.hypersistence.optimizer.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Vlad Mihalcea
 */
@SpringBootApplication
@ComponentScan(basePackages = "io.hypersistence.optimizer.forum")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
