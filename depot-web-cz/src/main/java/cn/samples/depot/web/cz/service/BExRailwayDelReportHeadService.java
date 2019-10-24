package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.railwayarrive.ArriveQuery;
import cn.samples.depot.web.bean.railwayarrive.ArriveReportDel4AddVo;
import cn.samples.depot.web.bean.railwayarrive.ArriveReportDel4DetailVo;
import cn.samples.depot.web.bean.railwayarrive.ArriveReportDel4UpdateVo;
import cn.samples.depot.web.entity.BExRailwayDelReportHead;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @Author majunzi
 * @Date 2019/8/26
 * @Description 海关业务-运抵作废
 **/
public interface BExRailwayDelReportHeadService extends IService<BExRailwayDelReportHead> {

    /**
     * 运抵报告作废
     *
     * @param id
     * @return
     */
    JsonResult declare(String id);

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 列表查询
     **/
    IPage<BExRailwayDelReportHead> page(ArriveQuery query, Integer pageNum, Integer pageSize);

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 查看详情
     **/
    ArriveReportDel4DetailVo detail(String id);

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 保存（新增）
     **/
    JsonResult save(ArriveReportDel4AddVo vo) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 编辑详情
     **/
    ArriveReportDel4UpdateVo detail4Update(String id) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/9/3
     * @Description 检查是否允许更新
     **/
    BExRailwayDelReportHead checkUpdate(String id) throws BizException;


    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 删除
     **/
    void deleteById(String id) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/27
     * @Description 删除运抵单
     **/
    void deleteByListId(String listId) throws BizException;
}