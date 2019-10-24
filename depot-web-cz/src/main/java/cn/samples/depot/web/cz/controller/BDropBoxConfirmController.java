package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.DropBoxStatus;
import cn.samples.depot.common.model.GoodsConfirmStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.BDropBoxQuery;
import cn.samples.depot.web.cz.service.BDropBoxConfirmDetailService;
import cn.samples.depot.web.cz.service.BDropBoxConfirmService;
import cn.samples.depot.web.cz.service.CStationAreaPositionsService;
import cn.samples.depot.web.cz.service.CStationAreasService;
import cn.samples.depot.web.entity.BDropBoxConfirm;
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
import static cn.samples.depot.web.cz.controller.BDropBoxConfirmController.API;

/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 落箱确认
 **/
@Api(tags = "堆场管理-落箱确认")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BDropBoxConfirmController {

    static final String API = "/BDropBoxConfirm";

    @Autowired
    private BDropBoxConfirmService service;
    @Autowired
    private BDropBoxConfirmDetailService detailService;
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
    // @Cacheable(value = "bDropBoxConfirm", sync = true)
    @ApiOperation(value = "分页查询")
    public JsonResult index(@Valid BDropBoxQuery bDropBoxPlanQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BDropBoxConfirm> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like(StringUtils.isNotEmpty(bDropBoxPlanQuery.getContainerNo()), BDropBoxConfirm::getContainerNo, bDropBoxPlanQuery.getContainerNo())
                .eq(StringUtils.isNotEmpty(bDropBoxPlanQuery.getStationAreaCode()), BDropBoxConfirm::getStationAreaCode, bDropBoxPlanQuery.getStationAreaCode())
                .eq(StringUtils.isNotEmpty(bDropBoxPlanQuery.getStatus()), BDropBoxConfirm::getStatus, bDropBoxPlanQuery.getStatus())
                .orderByDesc(BDropBoxConfirm::getCreateTime);


        // 查询第1页，每页返回10条
        Page<BDropBoxConfirm> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bDropBoxPlanQuery).orElseGet(BDropBoxQuery::new))
                .set("status", Lists.newArrayList(DropBoxStatus.values()))
                .set("entryStatus", Lists.newArrayList(GoodsConfirmStatus.values()))
                .set("stationArea", areasService.listMaps(new QueryWrapper<CStationAreas>().select("code", "name")))
                .set("stationAreaPosition", areaPositionsService.listMaps(new QueryWrapper<CStationAreaPositions>().select("code", "name"))));
    }

    /*
     * @Author majunzi
     * @Date 2019/8/5
     * @Description  落箱确认详情
     **/
    @AddLog
    // @Cacheable(value = "bDropBoxConfirm", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("落箱确认详情")
    public JsonResult detail(@PathVariable String id) throws Throwable {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id))
                .set("shipmentPlans", detailService.listByDropBoxConfirmId(id))
                .set("status", Lists.newArrayList(DropBoxStatus.values()))
                .set("entryStatus", Lists.newArrayList(GoodsConfirmStatus.values()))
                .set("stationArea", areasService.listMaps(new QueryWrapper<CStationAreas>().select("code", "name")))
                .set("stationAreaPosition", areaPositionsService.listMaps(new QueryWrapper<CStationAreaPositions>().select("code", "name"))));
    }

    /*
     * @Author majunzi
     * @Date 2019/8/5
     * @Description 新增落箱确认
     **/
    @AddLog
    @GetMapping(value = "/createConfirm" + ID)
    @ApiOperation(value = "新增落箱确认", notes = "新增落箱确认")
    @JsonView(value = {BDropBoxConfirm.View.Confirm.class})
    public JsonResult createConfirm(@PathVariable String id) {
        return JsonResult.data(M_DETAIL, BDropBoxConfirm.builder().id(id).build());
    }

    /*
     * @Author majunzi
     * @Date 2019/8/5
     * @Description 落箱确认
     **/
    @AddLog
    @PostMapping(value = "/confirm")
    @ApiOperation(value = "落箱确认", notes = "落箱确认")
    @JsonView(value = {BDropBoxConfirm.View.Confirm.class})
    public JsonResult confirm(@RequestBody BDropBoxConfirm bDropBoxConfirm) throws Throwable {
        service.confirm(bDropBoxConfirm);
        return JsonResult.success();
    }


}