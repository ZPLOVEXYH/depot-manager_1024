/**
 * @filename:BRailwayLoadListServiceImpl 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.cz.mapper.BRailwayLoadListMapper;
import cn.samples.depot.web.cz.service.BExRailwayListService;
import cn.samples.depot.web.cz.service.BRailwayLoadListService;
import cn.samples.depot.web.entity.BExRailwayList;
import cn.samples.depot.web.entity.BRailwayLoadConta;
import cn.samples.depot.web.entity.BRailwayLoadList;
import cn.samples.depot.web.entity.BRailwayLoadReportHead;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 装车记录单申报报文表体运抵单信息——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
public class BRailwayLoadListServiceImpl extends ServiceImpl<BRailwayLoadListMapper, BRailwayLoadList> implements BRailwayLoadListService {

    @Autowired
    private BExRailwayListService arriveService;

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 根据运抵编号，查询表头id集合
     **/
    @Override
    public Set<String> listHeadIdsByPartArriveNo(String partArriveNo) {
        return listMaps(new LambdaQueryWrapper<BRailwayLoadList>()
                .select(BRailwayLoadList::getRailwayLoadReportHeadId)
                .like(BRailwayLoadList::getArriveNo, partArriveNo))
                .stream().map(map -> map.get("railway_load_report_head_id").toString())
                .collect(Collectors.toSet());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 保存运抵单之前的一些 操作
     **/
    @Override
    public void saveBefore(BRailwayLoadReportHead head, BRailwayLoadConta conta, BRailwayLoadList list) throws BizException {
        if (StringUtils.isEmpty(list.getArriveNo())) throw new BizException("运抵编号不能为空");
        BExRailwayList arrive = arriveService.getByArriveNo(list.getArriveNo());
        if (null == arrive) throw new BizException(String.format("运抵编号[%s]不存在", list.getArriveNo()));
        list.setRailwayLoadReportHeadId(head.getId());
        list.setRailwayLoadReportContaId(conta.getId());
        list.setContaNo(conta.getContaNo());
        if (null == list.getCreateTime() || list.getCreateTime() <= 0) list.setCreateTime(System.currentTimeMillis());
    }
}