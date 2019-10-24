package cn.samples.depot.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 设置applicationContext
     * 覆盖ApplicationContextAware方法
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(SpringContextUtils.applicationContext)) {
            SpringContextUtils.applicationContext = applicationContext;
        }
    }

    /**
     * 获取applicationContext
     *
     * @return applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据bean名称获取bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
//        getApplicationContext().getBean("appKeyJpaRepo");
        return getApplicationContext().getBean(name);
    }

    /**
     * 根据bean class获取bean
     *
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 根据bean名称和bean class获取bean
     *
     * @param name
     * @param clazz
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
