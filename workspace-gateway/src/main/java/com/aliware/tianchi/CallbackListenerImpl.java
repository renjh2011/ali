package com.aliware.tianchi;

import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.listener.CallbackListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daofeng.xjf
 *
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 *
 */
public class CallbackListenerImpl implements CallbackListener {
    public static Map<Integer,Integer> TOTAL_MAP =  new ConcurrentHashMap<>(3);
//    public static Map<Integer,Integer> ACTIVE_MAP =  new ConcurrentHashMap<>(3);
//    public volatile static Map<Integer,ProviderStatus> STATUS_MAP =  new ConcurrentHashMap<>(3);

    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackListenerImpl.class);
    @Override
    public void receiveServerMsg(String msg) {
        if(msg!=null && !msg.isEmpty()){
            //解析provider端返回的线程池状态
            String[] status = msg.split(",");
            if(status!=null && status.length==3){
                //端口
                int port = Integer.parseInt(status[0]);
                //活跃数
                int activeCount = Integer.parseInt(status[1]);
                //总数
                int total = Integer.parseInt(status[2]);
//                ProviderStatus pStatus =  new ProviderStatus();
//                pStatus.setActiveCount(activeCount);
//                pStatus.setPort(port);
//                pStatus.setTotal(total);
//                pStatus.setPeriod(System.currentTimeMillis());
//                STATUS_MAP.put(port,pStatus);
                TOTAL_MAP.put(port,total);
//                CustomRobin.init(TOTAL_MAP);
                /*if(TOTAL_MAP.isEmpty()){
                    TOTAL_MAP.put(port,total);
                }
                ACTIVE_MAP.put(port,activeCount);*/
            }
        }
    }

}
