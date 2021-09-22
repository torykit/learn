package com.example.learn.cloud.feign.config;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RetryableRequestContext;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-09-14
 */
@Component
public class ZkServiceInstanceListSupplier implements ServiceInstanceListSupplier {

    @Override
    public String getServiceId() {
        return "";
    }


    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        List<ServiceInstance> serviceInstances = new ArrayList<>();
        serviceInstances.add(new DefaultServiceInstance("in-1", "producer", "localhost", 7000, false));
        serviceInstances.add(new DefaultServiceInstance("in-2", "producer", "localhost", 7001, false));
        serviceInstances.add(new DefaultServiceInstance("in-3", "app", "localhost", 7001, false));

        String host = ((RetryableRequestContext) request.getContext()).getClientRequest().getUrl().getHost();

        System.out.println(host);
        return Flux.just(serviceInstances.parallelStream().filter(s->s.getServiceId().equals(host)).collect(Collectors.toList()));
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return null;
    }
}
