package com.stuben.monitop.client.proxy;

import java.io.Serializable;

/**
 * 监控的内容 , 使用spel表达式: <a href=
 * "https://docs.spring.io/spring/docs/4.3.10.RELEASE/spring-framework-reference/html/expressions.html">SPEL</a>
 */
public class MonitorProxyInfoResult implements Serializable {
    /**
     * 桩号
     */
    private Integer pileNo;
    /**
     * 哪个类(全限定名l)
     */
    private String classFullName;
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

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
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

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
