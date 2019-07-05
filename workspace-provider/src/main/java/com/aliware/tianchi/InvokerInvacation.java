package com.aliware.tianchi;

import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;

public class InvokerInvacation<T> {
    private Invoker<T> invoker;
    private Invocation invocation;

    public InvokerInvacation(Invoker<T> invoker, Invocation invocation) {
        this.invoker = invoker;
        this.invocation = invocation;
    }

    public Invoker<T> getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    public Invocation getInvocation() {
        return invocation;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }
}
