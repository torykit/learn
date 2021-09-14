package com.example.learn.cloud.feign;

import com.example.learn.cloud.feign.api.ProducerIndexApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-09-14
 */
@EnableFeignClients
@SpringBootApplication
public class CloudFeignApplication {


    public static void main(String[] args) {
        SpringApplication.run(CloudFeignApplication.class, args);
    }

}
