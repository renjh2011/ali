package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daofeng.xjf
 *
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String ip = invoker.getUrl().getIp();
        int port = invoker.getUrl().getPort();
        ClientStatus.requestCount(ip,port);
        try{
            Result result = invoker.invoke(invocation);
            return result;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        boolean isSuccess = true;
        String ip = invoker.getUrl().getIp();
        int port = invoker.getUrl().getPort();
        if(!result.hasException() && RobinLb.getServerMap().get(port)==null){
            String maxThreadPool = result.getAttachment(port+"");
            RobinLb.getRobinLb(port).set(Integer.parseInt(maxThreadPool),port);
        }
        if(result.hasException()){
            /*if((System.currentTimeMillis()-ClientStatus.getStatus(ip,port).lastFailedTime.get())<200) {
                RobinLb robinLb = RobinLb.getRobinLb(port);
                robinLb.fail(ip,port);
            }*/
            isSuccess=false;
        }
        ClientStatus.responseCount(ip,port, !isSuccess);
        return result;
    }
}
