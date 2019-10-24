package cn.samples.depot.web.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.bean.shipmentStatistics.ContainerDetail;
import cn.samples.depot.web.entity.BContainerInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author majunzi
 * @Date 2019/9/23
 * @Description 集装箱信息
 **/
public interface BContainerInfoService extends IService<BContainerInfo> {
    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 箱货详情 （b_shipment_container的id）
     **/
    ContainerDetail contaDetialByShipmentContaId(String shipmentContaId) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/9/20
     * @Description 箱货详情（b_container_info的id）
     **/
    ContainerDetail contaDetial(String contaId) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/9/24
     * @Description 最新一条记录
     **/
    BContainerInfo lastContaInfo(String contaNo, boolean nullException) throws BizException;

}