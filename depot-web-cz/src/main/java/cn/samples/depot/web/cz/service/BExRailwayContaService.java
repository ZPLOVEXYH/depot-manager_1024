package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.entity.BExRailwayConta;
import cn.samples.depot.web.entity.BExRailwayList;
import cn.samples.depot.web.entity.BExRailwayReportHead;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Author majunzi
 * @Date 2019/8/22
 * @Description 铁路-运抵申报-集装箱
 **/
public interface BExRailwayContaService extends IService<BExRailwayConta> {

    /**
     * @Author majunzi
     * @Date 2019/8/22
     * @Description 根据集装箱号（部分，like） 查询表头id集合
     **/
    Set<String> listHeadIdsByContaNo(String partOfContaNo);

    void saveBefore(BExRailwayReportHead head, BExRailwayList arrive, BExRailwayConta conta) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 根据运抵单id，获取集装箱集合
     **/
    List<BExRailwayConta> listContas(String listId);
}