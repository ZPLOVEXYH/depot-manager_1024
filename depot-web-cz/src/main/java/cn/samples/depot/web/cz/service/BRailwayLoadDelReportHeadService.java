/**
 * @filename:BRailwayLoadDelReportHeadService 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.load.LoadDelAddVo;
import cn.samples.depot.web.bean.load.LoadDelUpdateVo;
import cn.samples.depot.web.bean.load.LoadQuery;
import cn.samples.depot.web.entity.BRailwayLoadDelReportHead;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 装车记录单作废报文表头——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
public interface BRailwayLoadDelReportHeadService extends IService<BRailwayLoadDelReportHead> {

    /**
     * 单个删除装车记录单作废报文
     *
     * @param id
     * @return
     */
    JsonResult deleteLoadDelReportById(String id);

    /**
     * 查看指定装车记录单作废报文数据
     *
     * @param id
     * @return
     */
    LoadDelUpdateVo queryLoadDelReportById(String id);

    /**
     * 装车记录单申报
     *
     * @param id
     * @return
     */
    JsonResult declare(String id);

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 分页查询
     **/
    IPage<BRailwayLoadDelReportHead> page(LoadQuery query, Integer pageNum, Integer pageSize);

    /**
     * @Author majunzi
     * @Date 2019/8/30
     * @Description 保存
     **/
    JsonResult saveVo(LoadDelAddVo vo) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/9/2
     * @Description 检查是否允许 编辑，删除
     **/
    BRailwayLoadDelReportHead checkUpdate(String id) throws BizException;
}