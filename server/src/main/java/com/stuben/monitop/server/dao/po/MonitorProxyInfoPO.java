package com.stuben.monitop.server.dao.po;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 监控代理信息
 * </p>
 *
 */
@TableName("MonitorProxyInfo")
public class MonitorProxyInfoPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 桩号名称
     */
    @TableField("name")
    private String name;
    /**
     * 监控的程序名称
     */
    @TableField("appName")
    private String appName;
    /**
     * 桩号
     */
    @TableField("pileNo")
    private Integer pileNo;
    /**
     * 类的全限定名
     */
    @TableField("classFullName")
    private String classFullName;
    /**
     * 方法名称
     */
    @TableField("methodName")
    private String methodName;
    /**
     * 参数的SPEL表达式
     */
    @TableField("param")
    private String param;
    /**
     * 参数满足的条件的SPEL表达式
     */
    @TableField("condition")
    private String condition;
    /**
     * 是否有效
     */
    @TableField("enable")
    private Boolean enable;
    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("updateTime")
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

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

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getEnable() {
        return enable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MonitorProxyInfoPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", appName='" + appName + '\'' +
                ", pileNo=" + pileNo +
                ", classFullName='" + classFullName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", param='" + param + '\'' +
                ", condition='" + condition + '\'' +
                ", enable=" + enable +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
