package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.BExRailwayDelConta;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Author majunzi
 * @Date 2019/8/26
 * @Description 海关业务-运抵作废
 **/
public interface BExRailwayDelContaService extends IService<BExRailwayDelConta> {

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 根据集装箱id，查找运抵作废表头id
     **/
    Set<String> listHeadIdsByPartContaNo(String partContaNo);

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description
     **/
    Wrapper<BExRailwayDelConta> getHeadIdWrapper(String id);

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description
     **/
    Wrapper<BExRailwayDelConta> getListIdWrapper(String id);

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 根据运抵单id，查询集装箱集合
     **/
    List<BExRailwayDelConta> listContas(String listId);

}