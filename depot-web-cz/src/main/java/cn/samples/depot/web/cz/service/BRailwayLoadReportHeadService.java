/**
 * @filename:BRailwayLoadReportHeadService 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.load.LoadQuery;
import cn.samples.depot.web.bean.load.LoadReportVo;
import cn.samples.depot.web.bean.load.add.RspLoadReport;
import cn.samples.depot.web.entity.BRailwayLoadReportHead;
import cn.samples.depot.web.entity.xml.load.rsp.LoadMessage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 装车记录单申请报文表头——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
public interface BRailwayLoadReportHeadService extends IService<BRailwayLoadReportHead> {


    /*
     * @Author majunzi
     * @Date 2019/8/29
     * @Description  分页查询
     **/
    IPage<BRailwayLoadReportHead> page(LoadQuery query, Integer pageNum, Integer pageSize);


    /**
     * 单个删除装车记录单申请报文
     *
     * @param id
     * @return
     */
    void deleteLoadReportById(String id) throws BizException;

    /**
     * 查看指定装车记录单申请报文表头数据
     *
     * @param id
     * @return
     */
    RspLoadReport queryLoadReportById(String id);

    /**
     * 查看指定装车记录单申请报文表头数据
     *
     * @param id
     * @return
     */
    RspLoadReport queryLoadReportByIdForCancel(String id);

    /**
     * 装车记录单申报
     *
     * @param id
     * @return
     */
    JsonResult declare(String id);

    /**
     * 处理装车记录单申请、作废的回执报文文件类型
     *
     * @param loadMessage
     * @return
     */
    JsonResult xmlReturnLoadHandle(LoadMessage loadMessage);

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 新增-大保存
     **/
    JsonResult saveVo(LoadReportVo vo) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 编辑-详情
     **/
    LoadReportVo detail4Update(String id) throws BizException;

    void update(String id, LoadReportVo vo) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 根据报文编号获取实体
     **/
    BRailwayLoadReportHead getByMessageId(String messageId);

    /**
     * @Author majunzi
     * @Date 2019/9/16
     * @Description 下拉选择
     **/
    List<BRailwayLoadReportHead> select();
}