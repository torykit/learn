package com.example.learn.cloud.feign.api;

import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-09-14
 */
@RequestMapping("index")
@FeignClient(value = "producer")
public interface ProducerIndexApi {

    @GetMapping
    String index();

}
