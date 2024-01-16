package raf.sk.teretanaservis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication@EnableScheduling
public class TeretanaServisApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeretanaServisApplication.class, args);
    }

}
