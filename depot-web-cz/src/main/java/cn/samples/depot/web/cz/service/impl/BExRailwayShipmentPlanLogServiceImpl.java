package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.BExRailwayShipmentPlanLogMapper;
import cn.samples.depot.web.cz.service.BExRailwayShipmentPlanLogService;
import cn.samples.depot.web.entity.BExRailwayShipmentPlanLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 发运计划记录-运抵标志
 **/
@Service
public class BExRailwayShipmentPlanLogServiceImpl extends ServiceImpl<BExRailwayShipmentPlanLogMapper, BExRailwayShipmentPlanLog> implements BExRailwayShipmentPlanLogService {
    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 查找所有的 发运计划编号 集合
     **/
    @Override
    public List<String> listPlanNos() {
        return list().stream().map(item -> item.getShipmentPlanNo()).collect(Collectors.toList());
    }
}