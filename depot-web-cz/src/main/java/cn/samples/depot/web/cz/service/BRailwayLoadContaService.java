/**
 * @filename:BRailwayLoadContaService 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.load.LoadQuery;
import cn.samples.depot.web.entity.BRailwayLoadConta;
import cn.samples.depot.web.entity.BRailwayLoadReportHead;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @Description: 装车记录单申报报文表集装箱信息——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
public interface BRailwayLoadContaService extends IService<BRailwayLoadConta> {

    /**
     * 单个删除装车记录单申报报文表集装箱信息
     *
     * @param id
     * @return
     */
    JsonResult deleteLoadContaById(String id);


    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 根据 集装箱号，运抵单好，查询表头Id集合
     **/
    Set<String> listHeadIdsByLoadQuery(LoadQuery query);

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 集装箱保存前的检查工作
     **/
    void saveBefore(BRailwayLoadReportHead head, BRailwayLoadConta conta) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 作废-添加运单， 根据申报报文编号，查询可以作废的集装箱集合
     **/
    List<BRailwayLoadConta> listContas(String messageId);

    /**
     * @Author majunzi
     * @Date 2019/9/25
     * @Description 获取最新一条记录
     **/
    BRailwayLoadConta getLastByContaNo(String contaNo);
}