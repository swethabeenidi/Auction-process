package de.yieldlab.recruiting.rtb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RTB {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(RTB.class, args);
    }

}
