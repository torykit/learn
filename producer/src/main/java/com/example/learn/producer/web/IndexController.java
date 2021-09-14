package com.example.learn.producer.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-09-14
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Value("${server.port}")
    private Integer serverPort;

    @GetMapping
    public String index() {
        return "success from " + serverPort;
    }


}
