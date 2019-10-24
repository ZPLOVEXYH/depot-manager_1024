package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.constant.Constants;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.cz.mapper.BExRailwayContaMapper;
import cn.samples.depot.web.cz.service.BExRailwayContaService;
import cn.samples.depot.web.entity.BExRailwayConta;
import cn.samples.depot.web.entity.BExRailwayList;
import cn.samples.depot.web.entity.BExRailwayReportHead;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author majunzi
 * @Date 2019/8/22
 * @Description 铁路-运抵申报-集装箱
 **/
@Service
public class BExRailwayContaServiceImpl extends ServiceImpl<BExRailwayContaMapper, BExRailwayConta> implements BExRailwayContaService {
    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 根据集装箱号（部分，like） 查询表头id集合
     **/
    @Override
    public Set<String> listHeadIdsByContaNo(String partOfContaNo) {
        return listMaps(new LambdaQueryWrapper<BExRailwayConta>()
                .select(BExRailwayConta::getExRailwayReportHeadId)
                .like(BExRailwayConta::getContaNo, partOfContaNo))
                .stream().map(map -> map.get("ex_railway_report_head_id").toString())
                .collect(Collectors.toSet());
    }

    @Override
    public void saveBefore(BExRailwayReportHead head, BExRailwayList arrive, BExRailwayConta conta) throws BizException {
        checkEmpty(conta);
        conta.setExRailwayReportHeadId(head.getId());
        conta.setExRailwayListId(arrive.getId());
        conta.setArriveNo(arrive.getArriveNo());
        if (null == conta.getCreateTime() || conta.getCreateTime() <= 0)
            conta.setCreateTime(System.currentTimeMillis());
        if (StringUtils.isEmpty(conta.getSealNo())) {
            conta.setSealNum(0);
        } else {
            conta.setSealNum(conta.getSealNo().split(Constants.COMMA).length);
        }
    }

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 非空校验
     **/
    private void checkEmpty(BExRailwayConta conta) throws BizException {
        if (null == conta) throw new BizException("集装箱不能为空");
        if (StringUtils.isEmpty(conta.getContaNo())) throw new BizException("集装箱号不能为空");
        if (StringUtils.isEmpty(conta.getContaType())) throw new BizException("箱型不能为空");
    }

    @Override
    public List<BExRailwayConta> listContas(String listId) {
        return list(new LambdaQueryWrapper<BExRailwayConta>().eq(BExRailwayConta::getExRailwayListId, listId));
    }
}