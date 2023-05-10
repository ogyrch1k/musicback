package com.streaming.music;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = "com.streaming.music.controllers")
@ComponentScan(basePackages = "com.streaming.music.service")
@ComponentScan(basePackages = "com.streaming.music.repository")
@ComponentScan(basePackages = "com.streaming.music.conf")
@ComponentScan(basePackages = "com.streaming.music.jwt")
@ComponentScan(basePackages = "com.streaming.music.Utilities")
@ComponentScan(basePackages = "com.streaming.music.CustomDetails")

public class MusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class, args);
    }

}
