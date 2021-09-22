package com.example.learn.cloud.feign.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-09-14
 */
//@RequestMapping("index")
@FeignClient(value = "app")
public interface AppApi {

    @GetMapping("/index")
    String index();

}
