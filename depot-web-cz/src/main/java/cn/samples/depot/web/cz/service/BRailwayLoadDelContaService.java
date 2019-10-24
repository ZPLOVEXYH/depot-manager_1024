/**
 * @filename:BRailwayLoadDelContaService 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.bean.load.LoadQuery;
import cn.samples.depot.web.entity.BRailwayLoadConta;
import cn.samples.depot.web.entity.BRailwayLoadDelConta;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Description: 装车记录单作废报文表集装箱信息——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
public interface BRailwayLoadDelContaService extends IService<BRailwayLoadDelConta> {

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 根据集装箱id，删除集装箱
     **/
    void deleteByContaId(String contaId) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 添加运单
     **/
    void add(String headId, List<BRailwayLoadConta> contas) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 根据 集装箱号，运抵单好，查询表头Id集合
     **/
    Set<String> listHeadIdsByLoadQuery(LoadQuery query);
}