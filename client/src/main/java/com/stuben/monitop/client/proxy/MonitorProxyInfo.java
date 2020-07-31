package com.stuben.monitop.client.proxy;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 监控的内容 , 使用spel表达式: <a href=
 * "https://docs.spring.io/spring/docs/4.3.10.RELEASE/spring-framework-reference/html/expressions.html">SPEL</a>
 */
public class MonitorProxyInfo {
    /**
     * 桩号
     */
    private Integer pileNo;
    /**
     * 哪个类
     */
    private Class<?> clz;
    /**
     * 监控方法名
     */
    private String methodName;
    /**
     * 监控字段 , 使用SPEL表达式 , 字段使用params[i]指定, 如"params[0].num" , 表示监控第一个字段的num值
     */
    private String param;
    /**
     * 监控字段满足某些条件才会监控 , 使用SPEL表达式 , 字段使用params[i]指定 , 如 "params[0].id==1 && * params[1].name=='a'" ,
     * 表示'方法字段0的id等于1'且'方法字段1的name等于a'时监控才会生效,
     */
    private String condition;

    /**
     * 是否生效
     */
    private Boolean enable;

    public Integer getPileNo() {
        return pileNo;
    }

    public void setPileNo(Integer pileNo) {
        this.pileNo = pileNo;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getEnable() {
        return enable;
    }

    @Override
    public String toString() {
        return "MonitorProxyInfo{" + "pileNo=" + pileNo + ", clz=" + clz + ", methodName='" + methodName + '\'' + ", param='" + param + '\'' + ", condition='"
                + condition + '\'' + ", enable=" + enable + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MonitorProxyInfo that = (MonitorProxyInfo) o;

        return new EqualsBuilder()
                .append(clz, that.clz)
                .append(methodName, that.methodName)
                .append(param, that.param)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(clz)
                .append(methodName)
                .append(param)
                .toHashCode();
    }
}
