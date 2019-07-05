package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author daofeng.xjf
 *
 * 服务端过滤器
 * 可选接口
 * 用户可以在服务端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.PROVIDER)
public class TestServerFilter implements Filter {
    private final static ArrayBlockingQueue<InvokerInvacation> buffer = new ArrayBlockingQueue<>(ServerStatus.getMaxThreadPool()+100);
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try{
            InvokerInvacation invokerInvacation = new InvokerInvacation(invoker,invocation);
            buffer.offer(invokerInvacation);
            buffer.take();
            Result result = invoker.invoke(invocation);
            return result;
        }
        catch (InterruptedException e){
            throw new RpcException(e.getMessage());
        }catch (Exception e){
            throw e;
        }

    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        int port = invoker.getUrl().getPort();
        Map<String,String> map = new HashMap<>(1);
        map.put(port+"",ServerStatus.getMaxThreadPool()+"");
        result.addAttachments(map);
        return result;
    }

}
