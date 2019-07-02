package com.aliware.tianchi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

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
    private static volatile boolean isInit =false;
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        Invoker<T> invoker=null;
        /*
        服务器信息返回 使用负载萨算法
         */
        invoker = smoothSelect(invokers);
        return invoker;

    }

    private <T> Invoker<T> smoothSelect(List<Invoker<T>> invokers) {
        if(invokers.size()>CallbackListenerImpl.TOTAL_MAP.size()){
            return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
        }else if(invokers.size()==CallbackListenerImpl.TOTAL_MAP.size() && !isInit){
            synchronized (UserLoadBalance.class) {
                CustomRobin.init(CallbackListenerImpl.TOTAL_MAP);
                isInit=true;
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

//    private <T> Invoker<T> randomSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
//        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
//    }
}
