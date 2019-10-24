package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.bean.BShipmentPlanDTO;
import cn.samples.depot.web.bean.BShipmentPlanQuery;
import cn.samples.depot.web.bean.railwayarrive.ArriveVo;
import cn.samples.depot.web.dto.shipment.BShipmentPlanMsg;
import cn.samples.depot.web.entity.BShipmentAuditLog;
import cn.samples.depot.web.entity.BShipmentPlan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 发运计划
 **/
public interface BShipmentPlanService extends IService<BShipmentPlan> {

    BShipmentPlan getByPlanNo(String planNo);

    /*
     * @Author majunzi
     * @Description  作废
     * @Date 2019/7/29
     **/
    boolean cancel(String id) throws Exception;

    /*
     * @Author majunzi
     * @Date 2019/7/29
     * @Description  审核
     **/
    boolean audit(BShipmentAuditLog log) throws Exception;

    /*
     * @Author majunzi
     * @Date 2019/7/29
     * @Description  处理企业端 发送的信息。新增或更新
     **/
    void handlePlanMsg(BShipmentPlanMsg planMsg);

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 查询 运抵报告中 可用的 发运计划
     **/
    List<BShipmentPlan> search4arrive(BShipmentPlanQuery bShipmentPlanQuery);

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 运抵-导入发运计划
     **/
    List<ArriveVo> import4arrive(List<String> ids);

    BShipmentPlanDTO detail(String id);

}