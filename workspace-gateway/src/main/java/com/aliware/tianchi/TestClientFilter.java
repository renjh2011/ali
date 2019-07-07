package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

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
        ClientStatus clientStatus = ClientStatus.requestCount(ip,port,true);
//        ClientStatus clientStatus = ClientStatus.getStatus(ip,port);
        if(clientStatus.activeCount.get()>clientStatus.maxThread.get()+5){
            ClientStatus.requestCount(ip, port,false);
            return null;
        }
        return invoker.invoke(invocation);
//        return result;
    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        String ip = invoker.getUrl().getIp();
        int port = invoker.getUrl().getPort();
        boolean isSuccess = true;
        if (!result.hasException()){
            ClientStatus clientStatus = ClientStatus.getStatus(ip, port);
            if(clientStatus.getStatus(ip, port).maxThread.get()==(Integer.MAX_VALUE-200)){
                String maxThreadPool = result.getAttachment(port + "");
                int max = Integer.parseInt(maxThreadPool);
                ClientStatus.getStatus(ip, port).maxThread.set(max);
            }
        }else {
            isSuccess=false;
        }
        ClientStatus.responseCount(ip, port, !isSuccess);
        return result;
    }

    /*public void test(){
        if (!result.hasException() && RobinLb.getServerMap().get(port) == null) {
            String maxThreadPool = result.getAttachment(port + "");
            RobinLb robinLb = RobinLb.getRobinLb(port);
            int max = Integer.parseInt(maxThreadPool);
            robinLb.set(max, port);
            ClientStatus.getStatus(ip, port).maxThread.set(max);
        }
        if (result.hasException()) {
            isSuccess = false;
            RobinLb robinLb = RobinLb.getRobinLb(port);
            robinLb.fail(port);
//            }

        }
    }*/
}
