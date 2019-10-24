package cn.samples.depot.web.cz.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.samples.depot.common.constant.MqQueueConstant;
import cn.samples.depot.common.utils.UniqueIdUtil;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 自定义mybatis插件，实现输出实际执行sql语句
 *
 * @author zhangpeng
 * @date 2019-09-26
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
@Slf4j
@Component
public class MybatisSqlInterceptor extends AbstractSqlParserHandler implements Interceptor {

    /**
     * 是否需要拦截，true是拦截，false是不拦截
     */
    @Value("#{'${sql.intercept:}'}")
    private Boolean intercept;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 忽略插入sql_log表的语句
     */
    @Value("#{'${ignore.sql.prefix:}'.split(',')}")
    private List<String> ignoreSqlPrefix;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!intercept) {
            return invocation.proceed();
        }
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // 获取SQL命令类型
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 如果是查询语句，那么排除掉
        if (SqlCommandType.SELECT.equals(sqlCommandType)) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String sql = boundSql.getSql().replaceAll("\\s+", " ").toLowerCase();
        if (CollectionUtil.isNotEmpty(ignoreSqlPrefix)) {
            for (int i = 0; i < ignoreSqlPrefix.size(); i++) {
                if (sql.toLowerCase().startsWith(ignoreSqlPrefix.get(i))) {
                    return invocation.proceed();
                }
            }
        }

        List<ParameterMapping> parameterMappings = new ArrayList<>(boundSql.getParameterMappings());
        Object parameterObject = boundSql.getParameterObject();
        if (parameterMappings.isEmpty() && parameterObject == null) {
            log.warn("parameterMappings is empty or parameterObject is null");
            return invocation.proceed();
        }

        Configuration configuration = mappedStatement.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        try {
            this.sqlParser(metaObject);

            String parameter = "null";
            MetaObject newMetaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() == ParameterMode.OUT) {
                    continue;
                }
                String propertyName = parameterMapping.getProperty();
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    parameter = getParameterValue(parameterObject);
                } else if (newMetaObject.hasGetter(propertyName)) {
                    parameter = getParameterValue(newMetaObject.getValue(propertyName));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    parameter = getParameterValue(boundSql.getAdditionalParameter(propertyName));
                }
                sql = sql.replaceFirst("\\?", parameter);
            }
            // 将拦截到的sql语句插入日志表中
            log.info("拦截到的sql语句为1：{}", sql);
        } catch (Exception e) {
            log.error(String.format("intercept sql error: [%s]", sql), e);
        }

        // 将拦截到的sql语句插入日志表中
        log.info("拦截到的sql语句为：{}", sql);
        rabbitTemplate.convertAndSend(MqQueueConstant.SQL_SYNC_EXCHANGE, MqQueueConstant.SQL_SYNC_ROUTING, sql, new CorrelationData(UniqueIdUtil.getUUID()));
        return invocation.proceed();
    }

    /**
     * 获取参数
     *
     * @param param Object类型参数
     * @return 转换之后的参数
     */
    private static String getParameterValue(Object param) {
        if (param == null) {
            return "null";
        }
        if (param instanceof Number) {
            return param.toString();
        }
        String value = null;
        if (param instanceof String) {
            value = param.toString();
        } else if (param instanceof Date) {
            DateUtil.format((Date) param, "yyyy-MM-dd HH:mm:ss");
        } else if (param instanceof IEnum) {
            value = String.valueOf(((IEnum) param).getValue());
        } else {
            value = param.toString();
        }
        return StringUtils.quotaMark(value);
    }


    @Override
    public Object plugin(Object o) {
        if (o instanceof StatementHandler) {
            return Plugin.wrap(o, this);
        }
        return o;
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
