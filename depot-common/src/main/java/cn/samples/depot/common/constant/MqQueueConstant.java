package cn.samples.depot.common.constant;

/**
 * Description: MQ 消息队列
 *
 * @className: MqQueueConstant
 * @Author: zhangpeng
 * @Date 2019/7/16 14:43
 * @Version 1.0
 **/
public interface MqQueueConstant {
    /**
     * 发运计划申请队列名称
     */
    String APPLY_QUEUE = "apply-queue";

    /**
     * 发运计划申请交换机名称
     */
    String APPLY_EXCHANGE = "apply-exchange";

    /**
     * 发运计划申请交换机路由名称
     */
    String APPLY_ROUTING = "apply.routing";

    /**
     * 发运计划审核回复队列名称
     */
    String AUDIT_QUEUE = "audit-queue";

    /**
     * 发运计划审核回复交换机名称
     */
    String AUDIT_EXCHANGE = "audit-exchange";

    /**
     * 审发运计划审核回复路由名称
     */
    String AUDIT_ROUTING = "audit.routing";

    /**
     * 操作日志队列名称
     */
    String OPERATION_LOG_QUEUE = "operation-log-queue";

    /**
     * 操作日志交换机名称
     */
    String OPERATION_LOG_EXCHANGE = "operation-log-exchange";

    /**
     * 操作日志路由名称
     */
    String OPERATION_ROUTING = "operation.routing";

    /**
     * sql语句同步交换机名称
     */
    String SQL_SYNC_QUEUE = "sql-sync-queue";

    /**
     * sql语句同步交换机名称
     */
    String SQL_SYNC_EXCHANGE = "sql-sync-exchange";

    /**
     * sql语句同步交换机路由名称
     */
    String SQL_SYNC_ROUTING = "sql-sync.routing";
}
