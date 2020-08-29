package org.game;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class App {

    public static void main(String[] args) {
        // nohup ./run > ../logs 2>&1 &
        SpringApplication.run(App.class, args);
    }
}
