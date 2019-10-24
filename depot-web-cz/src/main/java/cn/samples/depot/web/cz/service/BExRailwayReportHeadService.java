package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.railwayarrive.ArriveQuery;
import cn.samples.depot.web.bean.railwayarrive.ArriveReport4DetailVo;
import cn.samples.depot.web.bean.railwayarrive.ArriveReport4UpdateVo;
import cn.samples.depot.web.entity.BExRailwayReportHead;
import cn.samples.depot.web.entity.xml.ex.rsp.ExMessage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/8/22
 * @Description 铁路运抵申请报文表头
 **/
public interface BExRailwayReportHeadService extends IService<BExRailwayReportHead> {
    //列表
    IPage<BExRailwayReportHead> page(ArriveQuery query, Integer pageNum, Integer pageSize);

    //查看详情
    ArriveReport4DetailVo detail(String id);

    //新增-保存
    JsonResult save(ArriveReport4UpdateVo vo) throws BizException;

    //编辑详情
    ArriveReport4UpdateVo detail4Update(String id) throws BizException;

    //编辑-保存
    void update(String id, ArriveReport4UpdateVo vo) throws BizException;

    //删除
    void deleteById(String id) throws BizException;

    /**
     * 处理运抵申请和运抵作废的回执报文
     *
     * @param exMessage
     * @return
     */
    JsonResult xmlReturnExHandle(ExMessage exMessage);

    /**
     * 运抵报告申报
     *
     * @param id
     * @return
     */
    JsonResult declare(String id);


    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 根据报文编号，获取vo
     **/
    ArriveReport4UpdateVo getVoByMessageId(String messageId);

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 下拉选择
     **/
    List<BExRailwayReportHead> select();
}