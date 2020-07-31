package com.stuben.monitop.client.common;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.stuben.monitop.client.proxy.MonitorProxyInfo;
import com.stuben.monitop.client.proxy.MonitorProxyUtils;

public class MonitorContext {
    private static final Map<Integer/* pileNo */, Context> CONTEXT_MAP = new ConcurrentHashMap<>();
    private static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    private static final Boolean THE_TRUE = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorContext.class);

    public static MonitorProxyInfo put(MonitorProxyInfo monitorProxyInfo) {
        Expression paramExpr = spelExpressionParser.parseExpression(monitorProxyInfo.getParam());
        Expression conditionExpr = null;
        if (StringUtils.isNotBlank(monitorProxyInfo.getCondition())) {
            conditionExpr = spelExpressionParser.parseExpression(monitorProxyInfo.getCondition());
        }

        Context newContext = new Context(monitorProxyInfo, paramExpr, conditionExpr);

        Context oldContext = CONTEXT_MAP.put(monitorProxyInfo.getPileNo(), newContext);

        return null == oldContext ? null : oldContext.monitorProxyInfo;
    }

    public static void remove(MonitorProxyInfo monitorProxyInfo) {
        CONTEXT_MAP.remove(monitorProxyInfo.getPileNo());
    }

    public static List<MonitorProxyInfo> listProxyInfoByClz(Class clz) {
        return CONTEXT_MAP.values().stream().map(e -> e.monitorProxyInfo).filter(e -> e.getClz().equals(clz)).collect(Collectors.toList());
    }

    public static Integer getNum(int pileNo, Object[] args) {
        Integer num = null;
        Params params = new Params(args);
        Expression conditionExpr = CONTEXT_MAP.get(pileNo).conditionExpr;

        if (null == conditionExpr || THE_TRUE.equals(conditionExpr.getValue(params))) {
            Expression paramExpr = CONTEXT_MAP.get(pileNo).paramExpr;
            num = MonitorProxyUtils.objectToInt(paramExpr.getValue(params));
            if (null == num) {
                LOGGER.warn("paramExpression , param is not number");
            }
        }
        return num;
    }



    static class Context {
        MonitorProxyInfo monitorProxyInfo;
        Expression paramExpr;
        Expression conditionExpr;

        Context(MonitorProxyInfo monitorProxyInfo, Expression paramExpr, Expression conditionExpr) {
            this.monitorProxyInfo = monitorProxyInfo;
            this.paramExpr = paramExpr;
            this.conditionExpr = conditionExpr;
        }
    }

    static class Params {
        private Object[] params;

        Params(Object[] params) {
            this.params = params;
        }

        public Object[] getParams() {
            return params;
        }

        public void setParams(Object[] params) {
            this.params = params;
        }
    }
}
