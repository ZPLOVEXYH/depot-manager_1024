package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.model.ShipmentPlanStatus;
import cn.samples.depot.web.cz.mapper.BShipmentContainerMapper;
import cn.samples.depot.web.cz.service.BShipmentContainerService;
import cn.samples.depot.web.entity.BShipmentContainer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/8/8
 * @Description 集装箱信息表
 **/
@Service
public class BShipmentContainerServiceImpl extends ServiceImpl<BShipmentContainerMapper, BShipmentContainer> implements BShipmentContainerService {
    @Autowired
    BShipmentContainerMapper mapper;

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 根据发运计划id获取集装箱集合
     **/
    @Override
    public List<BShipmentContainer> listByShipmentPlanId(String shipmentId) {
        QueryWrapper<BShipmentContainer> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BShipmentContainer::getShipmentPlanId, shipmentId);
        return mapper.selectList(wrapper);
    }

    /**
     * @Author majunzi
     * @Date 2019/9/24
     * @Description 更新状态
     **/
    @Override
    public boolean updateStatus(String shipmentPlanId, String contaNo, ShipmentPlanStatus status) {
        return update(new LambdaUpdateWrapper<BShipmentContainer>()
                .set(BShipmentContainer::getStatus, status.getValue())
                .eq(BShipmentContainer::getShipmentPlanId, shipmentPlanId)
                .eq(BShipmentContainer::getContainerNo, contaNo));
    }

    /**
     * @Author majunzi
     * @Date 2019/9/24
     * @Description 根据发运计划id和状态 获取集合
     **/
    @Override
    public List<BShipmentContainer> listByShipmentPlanIdAndStatus(String shipmentPlanId, String... statuses) {
        return list(new LambdaQueryWrapper<BShipmentContainer>().eq(BShipmentContainer::getShipmentPlanId, shipmentPlanId).in(BShipmentContainer::getStatus, statuses));
    }
}