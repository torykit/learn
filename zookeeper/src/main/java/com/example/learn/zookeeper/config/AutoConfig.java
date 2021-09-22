package com.example.learn.zookeeper.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-01-06
 */
@Slf4j
@Configuration
public class AutoConfig {

    @Value("${spring.zookeeper.address}")
    private  String connectString;

    @Value("${spring.zookeeper.timeout}")
    private  int timeout;


    private ZooKeeper zooKeeper=null;


    @PostConstruct
    public void init() {
    }

    @Bean(name = "zkClient")
    public ZooKeeper zkClient(){

        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            //连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
            //  可指定多台服务地址 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
            zooKeeper = new ZooKeeper(connectString, timeout, event -> {
                log.info("【获取到状态: {}】", event.getState().name());

                if(Watcher.Event.KeeperState.SyncConnected==event.getState()){
                    //如果收到了服务端的响应事件,连接成功
                    countDownLatch.countDown();
                    log.info("【收到了Zookeeper服务端的响应事件,连接成功】");
                } else if (Watcher.Event.KeeperState.Disconnected == event.getState()) {
                    log.warn("【当前zookeeper服务连接已关闭！准备关闭当前服务】");

                }
            });
            countDownLatch.await();
            log.info("【初始化ZooKeeper连接状态....】={}",zooKeeper.getState());

        }catch (Exception e){
            log.error("【初始化ZooKeeper连接异常....】",e);
        }

        return  zooKeeper;
    }


    @PreDestroy
    public void destroy() throws InterruptedException {
        log.info("【准备关闭Zookeeper连接】");
        if (zooKeeper != null) {
            zooKeeper.close(10);
            log.info("【Zookeeper关闭成功！】");
        }
    }
}
