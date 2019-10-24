package cn.samples.depot.common.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Description:
 *
 * @className: MybatisPlusConfig
 * @Author: zhangpeng
 * @Date 2019/7/16 14:05
 * @Version 1.0
 **/
@EnableTransactionManagement
@Configuration
@MapperScan("cn.samples.deport.web.mapper")
public class MybatisPlusConfig {

    /**
     * mybatis-plus 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * SQL执行效率插件
     * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

}