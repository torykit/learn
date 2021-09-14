package com.example.learn.cloud.feign.config;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-09-14
 */
@Configuration
public class LoadBalancerConfig {

//    @Bean
    public ServiceInstanceListSupplier serviceInstanceListSupplier() {


        return null;
    }

}
