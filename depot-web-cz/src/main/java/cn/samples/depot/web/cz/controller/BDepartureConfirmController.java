package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.departureconfirm.BDepartureConfirmQuery;
import cn.samples.depot.web.bean.departureconfirm.BDepartureConfirmVo;
import cn.samples.depot.web.bean.departureconfirm.DepartureStatus;
import cn.samples.depot.web.cz.service.BDepartureConfirmService;
import cn.samples.depot.web.entity.BDepartureConfirm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BDepartureConfirmController.API;

/**
 * @Author majunzi
 * @Date 2019/8/21
 * @Description 发车确认
 **/
@Api(tags = "作业管理-发车确认", value = "发车确认")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BDepartureConfirmController {

    static final String API = "/BDepartureConfirm";

    @Autowired
    private BDepartureConfirmService service;

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 分页查询
     **/
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public JsonResult index(@Valid BDepartureConfirmQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BDepartureConfirm> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like((StringUtils.isNotEmpty(query.getContaNo())), BDepartureConfirm::getContaNo, query.getContaNo())
                .ge((null != query.getStartDepartureTime() && query.getStartDepartureTime() > 0), BDepartureConfirm::getDepartureTime, query.getStartDepartureTime())
                .le((null != query.getEndDepartureTime() && query.getEndDepartureTime() > 0), BDepartureConfirm::getDepartureTime, query.getEndDepartureTime())
                .orderByDesc(BDepartureConfirm::getCreateTime);

        // 查询第1页，每页返回10条
        Page<BDepartureConfirm> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(query).orElseGet(BDepartureConfirmQuery::new))
                .set("status", Lists.newArrayList(DepartureStatus.values())));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 详情
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("详情")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id))
                .set("status", DepartureStatus.values()));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 保存
     **/
    @AddLog
    @PostMapping
    // @CachePut(value = "bDepartureConfirm", key = "#bDepartureConfirm.id")
    @ApiOperation(value = "保存")
    public JsonResult save(@RequestBody BDepartureConfirmVo bDepartureConfirmVo) throws Throwable {
        service.save(bDepartureConfirmVo);
        return JsonResult.success();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 批量新增
     **/
    @GetMapping(value = NEW)
    @ApiOperation(value = "批量新增", notes = "批量新增,集装箱下拉：GET /station/CStationAreaPositions/selectContaOnly")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BDepartureConfirmVo()));
    }

}