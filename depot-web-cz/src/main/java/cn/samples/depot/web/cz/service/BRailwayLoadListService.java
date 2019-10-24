/**
 * @filename:BRailwayLoadListService 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.entity.BRailwayLoadConta;
import cn.samples.depot.web.entity.BRailwayLoadList;
import cn.samples.depot.web.entity.BRailwayLoadReportHead;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * @Description: 装车记录单申报报文表体运抵单信息——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
public interface BRailwayLoadListService extends IService<BRailwayLoadList> {


    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 根据运抵编号，查询表头id集合
     **/
    Set<String> listHeadIdsByPartArriveNo(String partArriveNo);

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 保存运抵单之前的一些 操作
     **/
    void saveBefore(BRailwayLoadReportHead head, BRailwayLoadConta conta, BRailwayLoadList list) throws BizException;
}