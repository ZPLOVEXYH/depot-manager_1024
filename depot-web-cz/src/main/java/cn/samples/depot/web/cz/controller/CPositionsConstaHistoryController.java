/**
 * @filename:CPositionsConstaHistoryController 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.cz.service.CPositionsConstaHistoryService;
import cn.samples.depot.web.entity.CPositionsConstaHistory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.M_PAGE;
import static cn.samples.depot.common.utils.Controllers.URL_PRE_STATION;
import static cn.samples.depot.web.cz.controller.CPositionsConstaHistoryController.API;

/**
 * @Description: 堆位集装箱历史记录表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Api(tags = "堆场管理-堆位集装箱历史记录表", value = "堆位集装箱历史记录表")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class CPositionsConstaHistoryController {

    static final String API = "/CPositionsConstaHistory";

    @Autowired
    private CPositionsConstaHistoryService service;

    /**
     * @return PageInfo<CPositionsConstaHistory>
     * @explain 分页条件查询堆位集装箱历史记录表
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @GetMapping
    // @Cacheable(value = "cPositionsConstaHistory", sync = true)
    @ApiOperation(value = "分页查询")
    public JsonResult index(@Valid CPositionsConstaHistory cPositionsConstaHistory,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CPositionsConstaHistory> wrapper = new QueryWrapper<>();
        // wrapper.lambda()
        // .eq((bShipmentPlanQuery.getEnterprisesId() != null && Strings.EMPTY != bShipmentPlanQuery.getEnterprisesId()), BShipmentPlan::getEnterprisesId, bShipmentPlanQuery.getEnterprisesId());// 发运计划编号


        // 查询第1页，每页返回10条
        Page<CPositionsConstaHistory> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(cPositionsConstaHistory).orElseGet(CPositionsConstaHistory::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }


}