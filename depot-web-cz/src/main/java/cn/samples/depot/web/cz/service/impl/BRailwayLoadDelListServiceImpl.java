package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.BRailwayLoadDelListMapper;
import cn.samples.depot.web.cz.service.BRailwayLoadDelListService;
import cn.samples.depot.web.entity.BRailwayLoadDelList;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 装车记录单作废报文表体运抵单信息——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@Service
public class BRailwayLoadDelListServiceImpl extends ServiceImpl<BRailwayLoadDelListMapper, BRailwayLoadDelList> implements BRailwayLoadDelListService {


    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 根据集装箱id，获取运抵明细表
     **/
    @Override
    public List<BRailwayLoadDelList> listArrives(String contaId) {
        return list(new LambdaQueryWrapper<BRailwayLoadDelList>().eq(BRailwayLoadDelList::getRailwayLoadDelContaId, contaId));
    }

    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 根据运抵单编号(like)，获取表头Id集合
     **/
    @Override
    public Set<String> listHeadIdsByPartArriveNo(String partArriveNo) {
        return list(new LambdaQueryWrapper<BRailwayLoadDelList>().like(BRailwayLoadDelList::getArriveNo, partArriveNo))
                .stream().map(list -> {
                    return list.getRailwayLoadDelReportHeadId();
                }).collect(Collectors.toSet());
    }

}