package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.entity.BExRailwayList;
import cn.samples.depot.web.entity.BExRailwayReportHead;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Author majunzi
 * @Date 2019/8/22
 * @Description 铁路-运抵申报-运抵单
 **/
public interface BExRailwayListService extends IService<BExRailwayList> {

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 根据运抵单编号（部分，like） 获取 运抵报文表头id集合
     **/
    Set<String> listHeadIdsByPartArriveNo(String partOfArriveNo);

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description
     **/
    void saveBefore(BExRailwayReportHead head, BExRailwayList arrive) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 根据表头id，删除运抵单
     **/
    void removeByHeadId(String id);

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 保存
     **/
    void save(BExRailwayReportHead head, BExRailwayList arrive) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 根据运抵报文编号，获取运抵单集合
     **/
    List<BExRailwayList> listArrives(String messageAddId) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/9/25
     * @Description
     **/
    BExRailwayList getByShipmentPlanNo(String shipmentPlanNo);

    /**
     * @Author majunzi
     * @Date 2019/10/16
     * @Description 下拉选择（不筛状态）
     **/
    List<BExRailwayList> select();

    /**
     * @Author majunzi
     * @Date 2019/10/16
     * @Description 根据运抵编号 获取运抵单
     **/
    BExRailwayList getByArriveNo(String arriveNo);
}