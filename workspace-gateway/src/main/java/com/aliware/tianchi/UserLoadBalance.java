package com.aliware.tianchi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcStatus;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author daofeng.xjf
 *
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackListenerImpl.class);
    private static volatile boolean isInit =false;
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        Invoker<T> invoker =  smoothSelect(invokers);
        return invoker;
    }

    private <T> Invoker<T> smoothSelect(List<Invoker<T>> invokers) {
        if(!isInit){
            init(invokers);
        }
        for(Invoker<T> invoker : invokers){
            String ip = invoker.getUrl().getIp();
            int port = invoker.getUrl().getPort();
            ClientStatus clientStatus = ClientStatus.getStatus(ip,port);
            if(System.currentTimeMillis()-clientStatus.failedTime.get()<200 || clientStatus.rtt.get()>400){
                int originalWeight = CustomRobin.get(port).getWeight().get();
                CustomRobin.setWeight(port,originalWeight-5);
            }
        }

        Integer port = CustomRobin.getServer();
        if(port==null){
            return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
        }
        for(Invoker<T> invoker : invokers){
            if(invoker.getUrl().getPort()==port){
                return invoker;
            }
        }
        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }

    private synchronized <T> void init(List<Invoker<T>> invokers) {
        for(Invoker<T> invoker : invokers){
            String ip = invoker.getUrl().getIp();
            int port = invoker.getUrl().getPort();
            CustomRobin.init(port,port,50,0);
        }
        isInit=true;
    }

//    private <T> Invoker<T> randomSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
//        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
//    }
}
