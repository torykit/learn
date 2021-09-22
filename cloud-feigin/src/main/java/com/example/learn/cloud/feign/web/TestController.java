package com.example.learn.cloud.feign.web;

import com.example.learn.cloud.feign.api.AppApi;
import com.example.learn.cloud.feign.api.ProducerIndexApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-09-14
 */
@RestController
@RequestMapping("test")
public class TestController {


    @Autowired
    private ProducerIndexApi producerIndexApi;
    @Autowired
    private AppApi appApi;

    @GetMapping("index")
    public String testIndex() {
        return producerIndexApi.index();
    }


    @GetMapping("index2")
    public String testIndex2() {
        return appApi.index();
    }

}
