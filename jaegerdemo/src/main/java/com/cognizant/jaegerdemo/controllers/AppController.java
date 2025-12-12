package com.cognizant.jaegerdemo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@RestController
@RequestMapping("/customers")
@Slf4j
public class AppController {

    Random random = new Random();

    private RestTemplate restTemplate;
    @Value("${spring.application.name}")
    private String applicationName;
    public AppController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/path1")
    public ResponseEntity<String> path1() {
        log.info("Incoming request at {} for request /path1 ", applicationName);
        doNothingButSleepForSomeTime();
        String response = restTemplate.getForObject("http://localhost:7078/customers/path2", String.class);
        doNothingButSleepForSomeTime();
        return ResponseEntity.ok("response from /path1 + " + response);
    }

    @GetMapping("/path2")
    public ResponseEntity<String>  path2() {
        log.info("Incoming request at {} at /path2", applicationName);
        doNothingButSleepForSomeTime();
        String response = restTemplate.getForObject("https://jsonplaceholder.typicode.com/users", String.class);
        doNothingButSleepForSomeTime();

        return ResponseEntity.ok( response);
    }

    public void doNothingButSleepForSomeTime() {
        try {
            int sleepTime = random.nextInt(2, 5);
            log.info("sleeping for " + sleepTime + " seconds");
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
