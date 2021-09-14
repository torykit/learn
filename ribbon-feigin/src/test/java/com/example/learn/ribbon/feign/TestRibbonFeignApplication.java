package com.example.learn.ribbon.feign;

import com.netflix.client.ClientException;
import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.client.http.RestClient;
import feign.Feign;
import feign.ribbon.RibbonClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-09-14
 */
@SpringBootTest
public class TestRibbonFeignApplication {

    @Test
    public void balancerTest() {
        ILoadBalancer balancer = new BaseLoadBalancer();
        List<Server> servers = new ArrayList<Server>();
        servers.add(new Server("localhost", 8081));
        servers.add(new Server("localhost", 8082));
        balancer.addServers(servers);

        for(int i=0;i<10;i++){
            Server choosedServer = balancer.chooseServer(null);
            System.out.println(choosedServer);
        }
    }


    @Test
    public void customerBalancerTest() throws Exception {

        ConfigurationManager.getConfigInstance().setProperty("providers.ribbon.listOfServers",
                "localhost:7000,localhost:7001");
        ConfigurationManager.getConfigInstance().setProperty("providers.ribbon.NFLoadBalancerRuleClassName",
                RoundRobinRule.class.getName());
        RestClient client = (RestClient) ClientFactory.getNamedClient("providers");
        HttpRequest request = HttpRequest.newBuilder().uri("/index").build();
        for(int i=0;i<10;i++){
            HttpResponse response = client.executeWithLoadBalancer(request);
            System.out.println("loop "+i+" run result -> "+response.getEntity(String.class));
        }
    }


    public void configFileBalancerTest() throws Exception{
        //加载配置文件
        ConfigurationManager.loadPropertiesFromResources("providers.properties");
        //UserService
//        UserService userService = Feign.builder().client(RibbonClient.create())
//                //.encoder(new JacksonEncoder())
//                //.decoder(new JacksonDecoder())
//                .target(UserService.class,"http://providers");
//        for(int i=0;i<10;i++){
//            String result = userService.index();
//            System.out.println("loop "+i+",result -> "+result);
//        }

    }

}
