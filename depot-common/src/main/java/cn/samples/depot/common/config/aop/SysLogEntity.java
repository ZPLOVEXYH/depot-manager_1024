package cn.samples.depot.common.config.aop;

import java.io.Serializable;

/**
 * Description:
 *
 * @className: SysLogEntity
 * @Author: zhangpeng
 * @Date 2019/7/16 14:04
 * @Version 1.0
 **/
public class SysLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // 用户操作
    private String operation;
    // 用户操作
    private String optInfo;
    // 请求方法
    private String method;
    // 请求参数
    private String params;
    // 返回结果
    private String rsp;
    // IP地址
    private String ip;
    // 创建时间
    private String createDate;
    private Long time;

    public String getOptInfo() {
        return optInfo;
    }

    public void setOptInfo(String optInfo) {
        this.optInfo = optInfo;
    }

    public String getRsp() {
        return rsp;
    }

    public void setRsp(String rsp) {
        this.rsp = rsp;
    }

    /**
     * 设置：用户操作
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * 获取：用户操作
     */
    public String getOperation() {
        return operation;
    }

    /**
     * 设置：请求方法
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 获取：请求方法
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置：请求参数
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 获取：请求参数
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置：IP地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取：IP地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置：创建时间
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取：创建时间
     */
    public String getCreateDate() {
        return createDate;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SysLogEntity [operation=" + operation + ", method=" + method + ", params=" + params + ", rsp=" + rsp
                + ", ip=" + ip + ", createDate=" + createDate + ", time=" + time + "]";
    }
}
