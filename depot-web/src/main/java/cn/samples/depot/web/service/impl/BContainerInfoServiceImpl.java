package cn.samples.depot.web.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.bean.shipmentStatistics.ContainerDetail;
import cn.samples.depot.web.entity.BContainerBillInfo;
import cn.samples.depot.web.entity.BContainerInfo;
import cn.samples.depot.web.entity.BShipmentContainer;
import cn.samples.depot.web.mapper.BContainerInfoMapper;
import cn.samples.depot.web.service.BContainerBillInfoService;
import cn.samples.depot.web.service.BContainerHistoryService;
import cn.samples.depot.web.service.BContainerInfoService;
import cn.samples.depot.web.service.BShipmentContainerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author majunzi
 * @Date 2019/9/23
 * @Description 箱货信息
 **/
@Service
@Slf4j
public class BContainerInfoServiceImpl extends ServiceImpl<BContainerInfoMapper, BContainerInfo> implements BContainerInfoService {


    @Autowired
    BContainerHistoryService historyService;
    @Autowired
    BContainerBillInfoService billInfoService;
    @Autowired
    BShipmentContainerService shipmentContainerService;

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 箱货详情 （b_shipment_container的id） 基本信息+集装箱历史记录
     * 补充 发运计划编号，提运单号
     **/
    @Override
    public ContainerDetail contaDetialByShipmentContaId(String shipmentContaId) throws BizException {
        BContainerBillInfo billInfo = getContainerBillInfo(shipmentContaId);
        BContainerInfo contaInfo = getById(billInfo.getContainerId());
        //补充 发运计划编号，提运单号
        contaInfo.setShipmentPlanNo(billInfo.getShipmentPlanNo());
        contaInfo.setBillNo(billInfo.getBillNo());
        return ContainerDetail.builder()
                .head(contaInfo)
                .histories(historyService.listHistory(billInfo.getContainerId()))
                .build();

    }


    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 根据发运计划的集装箱id获取 集装箱状态id
     **/
    private BContainerBillInfo getContainerBillInfo(String shipmentContaId) throws BizException {
        //根据发运计划的集装箱id 查找对应记录
        BShipmentContainer shipmentContainer = shipmentContainerService.getById(shipmentContaId);
        if (null == shipmentContainer) return null;
        //根据集装箱号+发运计划id 获取集装箱状态记录
        BContainerBillInfo billInfo = billInfoService.getByShipmentIdAndContaNo(shipmentContainer.getShipmentPlanId(), shipmentContainer.getContainerNo());
        if (null == billInfo) {
            log.info(String.format("找不到BContainerBillInfo记录，发运计划ID[%s],集装箱号[%s]", shipmentContainer.getShipmentPlanId(), shipmentContainer.getContainerNo()));
            throw new BizException("找不到单货信息");
        }
        //获取集装箱状态id
        return billInfo;
    }

    /**
     * @Author majunzi
     * @Date 2019/9/20
     * @Description 箱货详情（b_container_info的id）  基本信息+单货信息+集装箱历史记录
     **/
    @Override
    public ContainerDetail contaDetial(String contaId) throws BizException {
        BContainerInfo contaInfo = getById(contaId);
        return ContainerDetail.builder()
                .head(contaInfo)
                .billInfo(billInfoService.listBillInfo(contaId))
                .histories(historyService.listHistory(contaId))
                .build();
    }

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 箱状态, 企业端：运抵放行，理货放行，装车记录放行 不实时查。
     **/
    @Override
    public BContainerInfo lastContaInfo(String contaNo, boolean nullException) throws BizException {
        BContainerInfo contaInfo = getOne(new LambdaQueryWrapper<BContainerInfo>().eq(BContainerInfo::getContaNo, contaNo).orderByDesc(BContainerInfo::getCreateTime));
        if (nullException && null == contaInfo) {
            log.warn(String.format("找不到箱[%s]状态记录", contaNo));
            throw new BizException("找不到集装箱状态记录");
        }
        return contaInfo;
    }


}