package cn.samples.depot.web.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T> extends IService<T> {
    IPage<T> selectPage(IPage<T> page, @Param("ew") Wrapper<T> queryWrapper);

    default List<String> getSortableField() {
        return null;
    }

    T empty();

    void copyProperties(T uploaded, T saved);

    default void check(T t) throws Throwable {
        checkEmpty(t);
        checkBiz(t);
    }

    default void checkEmpty(T t) throws Throwable {

    }

    default void checkBiz(T t) throws Throwable {

    }

    //查询
    T fetch(String id) throws Throwable;

    default void afterFetch(T t) throws Throwable {
    }

    Optional<T> load(String id);

    // 新增
    default void beforeSave(T t) throws Throwable {
    }

    default void beforeSave(T t, T o) throws Throwable {
    }

    default void afterSave(T t) throws Throwable {
    }

    T saveT(T t) throws Throwable;

    void saveBatchT(List<T> ts) throws Throwable;

    //更新
    default void beforeUpdate(String id, T uploaded) throws Throwable {
    }

    default void beforeUpdate(String id, T uploaded, T saved) throws Throwable {
    }

    T updateT(String id, T uploaded) throws Throwable;

    default void afterUpdate(String id, T uploaded, T saved) throws Throwable {
    }

    default void afterUpdate(String id, T uploaded) throws Throwable {
    }

    //删除
    default void beforeDelete(String id) {
    }

    default void afterDelete(String id) {

    }

    void deleteT(String id) throws Throwable;

}
