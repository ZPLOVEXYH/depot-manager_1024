package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.WayBillAuditStatus;
import cn.samples.depot.common.utils.UniqueIdUtil;
import cn.samples.depot.web.cz.mapper.BExRailwayDelListMapper;
import cn.samples.depot.web.cz.service.*;
import cn.samples.depot.web.entity.BExRailwayConta;
import cn.samples.depot.web.entity.BExRailwayDelConta;
import cn.samples.depot.web.entity.BExRailwayDelList;
import cn.samples.depot.web.entity.BExRailwayList;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author majunzi
 * @Date 2019/8/26
 * @Description 海关业务-运抵作废
 **/
@Service
public class BExRailwayDelListServiceImpl extends ServiceImpl<BExRailwayDelListMapper, BExRailwayDelList> implements BExRailwayDelListService {

    @Autowired
    BExRailwayDelContaService delContaService;
    @Autowired
    BExRailwayDelReportHeadService delHeadService;
    @Autowired
    BExRailwayContaService contaService;
    @Autowired
    BExRailwayListService listService;

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description
     **/
    @Override
    public Wrapper<BExRailwayDelList> getHeadIdWrapper(String id) {
        return new LambdaQueryWrapper<BExRailwayDelList>().eq(BExRailwayDelList::getExRailwayReportDelHeadId, id);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 根据运抵单，查找运抵作废表头id
     **/
    @Override
    public Set<String> listHeadIdsByPartArriveNo(String arriveNo) {
        Set<String> ids = new HashSet<>();
        List<Map<String, Object>> maps = listMaps(new LambdaQueryWrapper<BExRailwayDelList>().select(BExRailwayDelList::getExRailwayReportDelHeadId).like(BExRailwayDelList::getArriveNo, arriveNo));
        if (!CollectionUtils.isEmpty(maps)) {
            maps.forEach(map ->
                    ids.add(map.get("ex_railway_report_del_head_id").toString())
            );
        }
        return ids;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 新增运抵单作废
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(String headId, List<BExRailwayList> arrives) throws BizException {
        if (CollectionUtils.isEmpty(arrives)) return;
        delHeadService.checkUpdate(headId);
        //查看当前已经存在的运抵单，不能重复
        List<String> arriveNos = list(getHeadIdWrapper(headId)).stream().map(BExRailwayDelList::getArriveNo).collect(Collectors.toList());

        List<BExRailwayDelList> lists = new ArrayList<>();
        List<BExRailwayDelConta> contas = new ArrayList<>();
        for (BExRailwayList arrive : arrives) {
            if (arriveNos.contains(arrive.getArriveNo()))
                throw new BizException(String.format("运抵单编号[%s]已存在", arrive.getArriveNo()));
            BExRailwayDelList delArrive = buildDelListByList(headId, arrive);
            lists.add(delArrive);
            contas.addAll(buildDelContasByListId(headId, delArrive.getId(), arrive.getId()));
        }
        saveBatch(lists);
        delContaService.saveBatch(contas);
    }

    private List<BExRailwayDelConta> buildDelContasByListId(String headId, String delListId, String listId) {
        List<BExRailwayConta> contas = contaService.listContas(listId);
        return contas.stream().map(conta -> {
            BExRailwayDelConta delConta = BExRailwayDelConta.builder().build();
            BeanUtils.copyProperties(conta, delConta, "id");
            delConta.setExRailwayReportDelHeadId(headId);
            delConta.setExRailwayDelListId(delListId);
            delConta.setCreateTime(System.currentTimeMillis());
            return delConta;
        }).collect(Collectors.toList());
    }

    private BExRailwayDelList buildDelListByList(String headId, BExRailwayList arrive) throws BizException {
        BExRailwayList arrive2 = listService.getById(arrive.getId());
        if (null == arrive2) throw new BizException(String.format("找不到对应[%s]的运抵单", arrive.getId()));
        BeanUtils.copyProperties(arrive2, arrive, "notes");
        if (!WayBillAuditStatus.WayBillAudit_03.getValue().equals(arrive.getAuditStatus()))
            throw new BizException(String.format("运抵单[%s]当前状态不允许作废", arrive.getArriveNo()));
        BExRailwayDelList delList = BExRailwayDelList.builder().build();
        BeanUtils.copyProperties(arrive, delList);
        delList.setId(UniqueIdUtil.getUUID());
        delList.setExRailwayReportDelHeadId(headId);
        delList.setCreateTime(System.currentTimeMillis());
        delList.setAuditStatus(WayBillAuditStatus.WayBillAudit_05.getValue());
        return delList;
    }


}