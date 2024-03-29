package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {
    public static volatile int TOTAL = -1;

    public CallbackServiceImpl() {
        /*timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!listeners.isEmpty()) {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
                            DataStore dataStore = ExtensionLoader.getExtensionLoader(DataStore.class).getDefaultExtension();
                            Map<String, Object> map = dataStore.get(Constants.EXECUTOR_SERVICE_COMPONENT_KEY);
                            Set<Map.Entry<String, Object>> set = map.entrySet();
                            //线程池状态
                            String status = null;
                            for(Map.Entry<String, Object> entry1 : set){
                                if(entry1.getValue() instanceof ThreadPoolExecutor){
                                    ThreadPoolExecutor executorService = (ThreadPoolExecutor) entry1.getValue();
                                    TOTAL=executorService.getMaximumPoolSize();
                                    status=entry1.getKey()+","+executorService.getActiveCount()+","+executorService.getMaximumPoolSize();
                                    break;
                                }
                            }
                            if(status!=null) {
                                entry.getValue().receiveServerMsg(status);
                            }
                        } catch (Throwable t1) {
                            listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, 5000);*/
    }

//    private Timer timer = new Timer();

    /**
     * key: listener type
     * value: callback listener
     */
//    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    @Override
    public void addListener(String key, CallbackListener listener) {
//        listeners.put(key, listener);
//        listener.receiveServerMsg(new Date().toString()); // send notification for change
    }
}
