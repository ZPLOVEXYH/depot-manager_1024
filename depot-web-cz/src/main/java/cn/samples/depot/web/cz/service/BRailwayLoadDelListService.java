package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.BRailwayLoadDelList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Description: 装车记录单作废报文表体运抵单信息——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
public interface BRailwayLoadDelListService extends IService<BRailwayLoadDelList> {

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 根据集装箱id 查询运抵明细集合
     **/
    List<BRailwayLoadDelList> listArrives(String contaId);

    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 根据运抵编号，查询表头id集合
     **/
    Set<String> listHeadIdsByPartArriveNo(String partArriveNo);
}