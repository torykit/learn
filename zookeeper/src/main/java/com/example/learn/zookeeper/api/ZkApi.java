package com.example.learn.zookeeper.api;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author zhen.zhao01@trinasolar.com
 * @since 2021-01-06
 */
@Slf4j
@Component
public class ZkApi {


    @Autowired
    @Getter
    private ZooKeeper zkClient;


    /**
     * 判断指定节点是否存在
     * @param path
     * @param needWatch  指定是否复用zookeeper中默认的Watcher
     * @return
     */
    public Stat exists(String path, boolean needWatch){
        try {
            return zkClient.exists(path,needWatch);
        } catch (Exception e) {
            log.error("【断指定节点是否存在异常】{},{}",path,e);
            return null;
        }
    }

    /**
     *  检测结点是否存在 并设置监听事件
     *      三种监听类型： 创建，删除，更新
     *
     * @param path
     * @param watcher  传入指定的监听类
     * @return
     */
    public Stat exists(String path,Watcher watcher ){
        try {
            return zkClient.exists(path,watcher);
        } catch (Exception e) {
            log.error("【断指定节点是否存在异常】{},{}",path,e);
            return null;
        }
    }

    /**
     * 创建持久化节点
     * @param path
     * @param data
     */
    public boolean createNode(String path, String data){
        return createNode(path, data, CreateMode.PERSISTENT);
    }


    /**
     * 创建持久化节点
     * @param path
     * @param data
     */
    public boolean createNode(String path, String data, CreateMode createMode){
        try {
            zkClient.create(path,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
            return true;
        } catch (Exception e) {
            log.error("【创建持久化节点异常】{},{},{}",path,data,e);
            return false;
        }
    }


    /**
     * 创建临时节点
     * @param path
     * @param data
     */
    public boolean createTmpNode(String path, String data){
        try {
            zkClient.create(path,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            return true;
        } catch (Exception e) {
            log.error("【创建临时节点异常】{},{},{}",path,data,e);
            return false;
        }
    }

    /**
     * 尝试创建永久节点
     * @param path
     * @param data
     * @param createMode
     * @param success
     */
    public void tryCreateNode(String path, String data,CreateMode createMode, Consumer<String> success) {
        zkClient.exists(path, false, (rc, pa, ctx, stat) -> {
            if (KeeperException.Code.NONODE.intValue() == rc) {
                zkClient.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode,  (r, p, c, s) -> {
                    if (KeeperException.Code.OK.intValue() == r) {
                        success.accept(p);
                    }
                }, null);
            }
        }, null);
    }


    public void deepCreateNode(String path) {
        StringBuilder sb = new StringBuilder();
        for (String p : path.split("/")) {
            if (!StringUtils.hasText(p)) {
                continue;
            }
            sb.append("/");
            sb.append(p);
            log.trace("尝试创建node：{}", sb.toString());
            tryCreateNode(sb.toString(), "null", CreateMode.PERSISTENT, s -> {});
        }
    }


    public static void main(String[] args) {
        String s = "/abc/efg/hij";
        System.out.println(Arrays.toString(s.split("/")));

    }

    /**
     * 修改持久化节点
     * @param path
     * @param data
     */
    public boolean updateNode(String path, String data){
        return updateNode(path, data, -1);
    }

    public boolean updateNode(String path, String data, int version) {
        try {
            //zk的数据版本是从0开始计数的。如果客户端传入的是-1，则表示zk服务器需要基于最新的数据进行更新。如果对zk的数据节点的更新操作没有原子性要求则可以使用-1.
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zkClient.setData(path,data.getBytes(),version);
            return true;
        } catch (Exception e) {
            log.error("【修改持久化节点异常】{},{},{}",path,data,e);
            return false;
        }
    }


    /**
     * 删除持久化节点
     * @param path
     */
    public boolean deleteNode(String path){
        try {
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zkClient.delete(path,-1);
            return true;
        } catch (Exception e) {
            try {
                zkClient.delete(path,-1);
            } catch (InterruptedException | KeeperException interruptedException) {
                log.error("删除持久化节点重试失败！");
            }
            log.error("【删除持久化节点异常】{},{}",path,e);
            return false;
        }
    }

    /**
     * 删除持久化节点 不通知
     * @param path
     * @return
     */
    public boolean deleteNotNoWatch(String path) {
        try {
            //version参数指定要更新的数据的版本, 如果version和真实的版本不同, 更新操作将失败. 指定version为-1则忽略版本检查
            zkClient.delete(path,-1, (rc, path1, ctx) -> { }, null);
            return true;
        } catch (Exception e) {
            log.error("【删除持久化节点异常】{},{}",path,e);
            return false;
        }
    }

    /**
     * 获取节点
     * @param path
     * @return
     */
    public List<String> getChildren(String path) {
        List<String> list = new ArrayList<>();
        try {
            list = zkClient.getChildren(path, false);

        } catch (KeeperException e) {
            log.trace("获取children失败！", e);
        } catch (InterruptedException e) {
            log.error("获取数据错误!", e);
        }
        return list;
    }


    public int getAllChildrenNumber(String path) {
        try {
//            return zkClient.getAllChildrenNumber(path);
            return getChildren(path).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获取指定节点的值
     * @param path
     * @return
     */
    public  String getData(String path){
        try {
            Stat stat=new Stat();
            byte[] bytes=zkClient.getData(path,false,stat);
            return  new String(bytes);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    public <T> T getData(String path, Class<T> clazz) {
        String data = getData(path);
        if (!JSONUtil.isJson(data)) {
            log.error("错误的数据解析！zk路径：{}", path);
            return null;
        }
        return JSONUtil.toBean(data, clazz);
    }


    /**
     * 获取指定节点的值
     * @param path
     * @return
     */
    public  String getData(String path, Watcher watcher){
        try {
            Stat stat=new Stat();
            byte[] bytes=zkClient.getData(path,watcher,stat);
            return  new String(bytes);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }


}
