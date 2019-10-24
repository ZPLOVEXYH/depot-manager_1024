package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.BExRailwayShipmentPlanLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 发运计划记录-运抵标志
 **/
public interface BExRailwayShipmentPlanLogService extends IService<BExRailwayShipmentPlanLog> {
    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 查找所有的 发运计划编号 集合
     **/
    List<String> listPlanNos();
}