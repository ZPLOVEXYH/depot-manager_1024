package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.service.CRUDService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class CRUDServiceImpl<mybatisplus extends BaseMapper<T>, T> extends ServiceImpl<mybatisplus, T> implements CRUDService<T>, InitializingBean {

    // @Autowired
    // private BaseMapper<T> mybatisplus;

    @Override
    public T empty() {
        return null;
    }

    @Override
    public IPage<T> selectPage(IPage<T> page, @Param("ew") Wrapper<T> queryWrapper) {
        return page(page, queryWrapper);
    }

    @Override
    public void copyProperties(T uploaded, T saved) {
        BeanUtils.copyProperties(uploaded, saved);
    }

    @Override
    public T fetch(String id) throws Throwable {
        T t = load(id).orElseThrow(() -> new Exception("数据不存在"));
        afterFetch(t);
        return t;
    }

    @Override
    public Optional<T> load(String id) {
        return Optional.ofNullable(getById(id));
    }

    @Override
    @Transactional
    public T saveT(T t) throws Throwable {
        check(t);
        beforeSave(t);
        super.save(t);
        afterSave(t);
        return t;
    }


    @Override
    @Transactional
    public void saveBatchT(List<T> ts) throws Throwable {
        for (T t : ts) {
            check(t);
            beforeSave(t);
        }
        super.saveBatch(ts);
        for (T t : ts) {
            afterSave(t);
        }
    }

    @Override
    @Transactional
    public T updateT(String id, T uploaded) throws Throwable {
        beforeUpdate(id, uploaded);
        super.saveOrUpdate(uploaded);
        afterUpdate(id, uploaded);
        return uploaded;
    }

    @Override
    @Transactional
    public void deleteT(String id) throws Throwable {
        beforeDelete(id);
        removeById(id);
        afterDelete(id);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
