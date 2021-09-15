package com.example.learn.cloud.feign.config;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-09-14
 */
@Component
public class ZkServiceInstanceListSupplier implements ServiceInstanceListSupplier {

    @Override
    public String getServiceId() {
        return "producer";
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        List<ServiceInstance> serviceInstances = new ArrayList<>();
        serviceInstances.add(new DefaultServiceInstance("in-1", getServiceId(), "localhost", 7000, false));
        serviceInstances.add(new DefaultServiceInstance("in-2", getServiceId(), "localhost", 7001, false));

        return Flux.just(serviceInstances);
    }
}
