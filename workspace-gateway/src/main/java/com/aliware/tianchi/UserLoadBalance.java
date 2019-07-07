package com.aliware.tianchi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author daofeng.xjf
 *
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        Invoker<T> invoker =  smoothSelect(invokers);
//        System.out.println("invoker = [" + invoker + "]");
        return invoker;
    }

    private <T> Invoker<T> smoothSelect(List<Invoker<T>> invokers) {
        int max=Integer.MIN_VALUE;
        Invoker<T> maxInvoker= null;
        for(int i=invokers.size()-1;i>=0;i--){
            Invoker<T> invoker = invokers.get(i);
            URL invokerUrl= invoker.getUrl();
            ClientStatus clientStatus = ClientStatus.getStatus(invokerUrl.getIp(),invokerUrl.getPort());
            int free = clientStatus.maxThread.get()-clientStatus.activeCount.get();
            if(free>1){
                return invoker;
            }
            if(max<free){
                max=free;
                maxInvoker=invoker;
            }
        }
        return maxInvoker==null?invokers.get(ThreadLocalRandom.current().nextInt(invokers.size())):maxInvoker;
        /*if(isInit || invokers.size()== RobinLb.getServerMap().size()){
            isInit=true;
            Integer port = RobinLb.getServer();
            for(Invoker<T> invoker : invokers){
                if(invoker.getUrl().getPort()==port){
                    return invoker;
                }
            }
        }*/
//        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }
}
