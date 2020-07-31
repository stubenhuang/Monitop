package com.stuben.monitop.client.proxy;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.ApplicationContext;

/**
 * 监控装饰器
 */
public class MonitorProxyAdvisor extends DefaultPointcutAdvisor {

    // 装饰器的唯一标志
    private Integer pileNo;

    /**
     * 初始化
     */
    static MonitorProxyAdvisor init(MonitorProxyInfo proxyInfo, ApplicationContext applicationContext) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* " + proxyInfo.getMethodName() + "(..))");
        return new MonitorProxyAdvisor(applicationContext, proxyInfo, pointcut);
    }

    private MonitorProxyAdvisor(ApplicationContext applicationContext, MonitorProxyInfo proxyInfo, Pointcut pointcut) {
        super(pointcut, new MonitorProxyInterceptor(applicationContext, proxyInfo));
        this.pileNo = proxyInfo.getPileNo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MonitorProxyAdvisor that = (MonitorProxyAdvisor) o;

        return new EqualsBuilder().append(pileNo, that.pileNo).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(pileNo).toHashCode();
    }
}
