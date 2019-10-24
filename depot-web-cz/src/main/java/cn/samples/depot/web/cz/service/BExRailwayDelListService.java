package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.entity.BExRailwayDelList;
import cn.samples.depot.web.entity.BExRailwayList;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Author majunzi
 * @Date 2019/8/26
 * @Description 海关业务-运抵作废
 **/
public interface BExRailwayDelListService extends IService<BExRailwayDelList> {

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 根据运抵单，查找运抵作废表头id
     **/
    Set<String> listHeadIdsByPartArriveNo(String partArriveNo);

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description
     **/
    Wrapper<BExRailwayDelList> getHeadIdWrapper(String id);

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 新增运抵单作废
     **/
    void add(String headId, List<BExRailwayList> arrives) throws BizException;

}