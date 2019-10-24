package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.DropBoxStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.BDropBoxQuery;
import cn.samples.depot.web.cz.service.BDropBoxPlanDetailService;
import cn.samples.depot.web.cz.service.BDropBoxPlanService;
import cn.samples.depot.web.cz.service.CStationAreaPositionsService;
import cn.samples.depot.web.cz.service.CStationAreasService;
import cn.samples.depot.web.entity.BDropBoxPlan;
import cn.samples.depot.web.entity.CStationAreaPositions;
import cn.samples.depot.web.entity.CStationAreas;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
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
import static cn.samples.depot.web.cz.controller.BDropBoxPlanController.API;


/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 作业管理-落箱计划
 **/
@Api(tags = "作业管理-落箱计划")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BDropBoxPlanController {

    static final String API = "/BDropBoxPlan";

    @Autowired
    private BDropBoxPlanService service;
    @Autowired
    private BDropBoxPlanDetailService detailService;
    @Autowired
    private CStationAreasService areasService;
    @Autowired
    private CStationAreaPositionsService areaPositionsService;

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 分页查询
     **/
    @AddLog
    @GetMapping
    // @Cacheable(value = "bDropBoxPlan", sync = true)
    @ApiOperation(value = "分页查询")
    public JsonResult index(@Valid BDropBoxQuery bDropBoxPlanQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BDropBoxPlan> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(StringUtils.isNotEmpty(bDropBoxPlanQuery.getContainerNo()), BDropBoxPlan::getContainerNo, bDropBoxPlanQuery.getContainerNo())
                .eq(StringUtils.isNotEmpty(bDropBoxPlanQuery.getStationAreaCode()), BDropBoxPlan::getStationAreaCode, bDropBoxPlanQuery.getStationAreaCode())
                .eq(StringUtils.isNotEmpty(bDropBoxPlanQuery.getStatus()), BDropBoxPlan::getStatus, bDropBoxPlanQuery.getStatus())
                .orderByDesc(BDropBoxPlan::getCreateTime);


        // 查询第1页，每页返回10条
        Page<BDropBoxPlan> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bDropBoxPlanQuery).orElseGet(BDropBoxQuery::new))
                .set("status", Lists.newArrayList(DropBoxStatus.values()))
                .set("stationArea", areasService.listMaps(new QueryWrapper<CStationAreas>().select("code", "name")))
                .set("stationAreaPosition", areaPositionsService.listMaps(new QueryWrapper<CStationAreaPositions>().select("code", "name"))));
    }


    /*
     * @Author majunzi
     * @Date 2019/8/5
     * @Description  查看指定落箱计划及发运计划信息
     **/
    @AddLog
    // @Cacheable(value = "bDropBoxPlan", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id))
                .set("shipmentPlans", detailService.listByDropBoxPlanId(id))
                .set("status", Lists.newArrayList(DropBoxStatus.values()))
                .set("stationArea", areasService.listMaps(new QueryWrapper<CStationAreas>().select("code", "name")))
                .set("stationAreaPosition", areaPositionsService.listMaps(new QueryWrapper<CStationAreaPositions>().select("code", "name"))));
    }


    /*
     * @Author majunzi
     * @Date 2019/8/5
     * @Description 落箱安排
     **/
    @AddLog
    @GetMapping(value = "/createArrangement" + ID)
    @ApiOperation(value = "新增", notes = "新增落箱安排")
    @JsonView(value = {BDropBoxPlan.View.Arrangement.class})
    public JsonResult createArrangement(@PathVariable String id) {
        return JsonResult.data(M_DETAIL, BDropBoxPlan.builder().id(id).build());
    }

    /*
     * @Author majunzi
     * @Date 2019/8/5
     * @Description 落箱安排
     **/
    @AddLog
    @PostMapping(value = "arrangement")
    @ApiOperation(value = "落箱安排", notes = "落箱安排")
    @JsonView(value = {BDropBoxPlan.View.Arrangement.class})
    public JsonResult arrangement(@RequestBody BDropBoxPlan bDropBoxPlan) throws Throwable {
        service.arrangement(bDropBoxPlan);
        return JsonResult.success();
    }
}