package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.model.UseStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.stations.position.AreaPositionsQuery;
import cn.samples.depot.web.bean.stations.position.StationAreaPositionsQuery;
import cn.samples.depot.web.cz.service.CStationAreaPositionsService;
import cn.samples.depot.web.cz.service.CStationAreasService;
import cn.samples.depot.web.entity.CStationAreaPositions;
import cn.samples.depot.web.entity.CStationAreas;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.CStationAreaPositionsController.API;


/**
 * @Author majunzi
 * @Date 2019/10/15
 * @Description 堆位表接口层
 **/
@Api(tags = "基础信息-箱位管理+堆存查询", value = "堆位表")
@RestController
@RequestMapping(value = API)
@Slf4j
public class CStationAreaPositionsController {

    static final String API = URL_PRE_STATION + "/CStationAreaPositions";

    @Autowired
    private CStationAreaPositionsService service;
    @Autowired
    private CStationAreasService areasService;

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 查询
     **/
    @AddLog
    @GetMapping
//    @Cacheable(value = "cDischarges", sync = true)
    @ApiOperation(value = "查询")
    public JsonResult index(@Valid StationAreaPositionsQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CStationAreaPositions> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq((query.getCode() != null && Strings.EMPTY != query.getCode()), CStationAreaPositions::getCode, query.getCode()) // code全匹配查询
                .eq((query.getStationAreaCode() != null && Strings.EMPTY != query.getStationAreaCode()), CStationAreaPositions::getStationAreaCode, query.getStationAreaCode())
                .orderByDesc(CStationAreaPositions::getCreateTime);


        // 查询第1页，每页返回10条
        Page<CStationAreaPositions> page = new Page<>(pageNum, pageSize);
        IPage<CStationAreaPositions> iPage = service.page(page, wrapper);
        if (null != iPage) {
            List<CStationAreaPositions> cStationAreaPositionsList = iPage.getRecords();
            if (CollectionUtils.isNotEmpty(cStationAreaPositionsList)) {
                cStationAreaPositionsList.forEach(cStationAreaPositions -> {
                    // 获取得到堆区编号
                    String stationAreaCode = cStationAreaPositions.getStationAreaCode();
                    if (StringUtils.isNotEmpty(stationAreaCode)) {
                        CStationAreas cStationAreas = areasService.getOne(Wrappers.<CStationAreas>lambdaQuery().eq(CStationAreas::getCode, stationAreaCode)
                                .eq(CStationAreas::getEnable, Status.ENABLED.getValue()));

                        cStationAreaPositions.setStationAreaName(cStationAreas.getName());
                    }

                });

            }
        }

        return JsonResult.data(Params.param(M_PAGE, iPage)
                .set("query", Optional.ofNullable(query).orElseGet(StationAreaPositionsQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED))
                .set("useStatus", Lists.newArrayList(UseStatus.NOT_USED, UseStatus.USED, UseStatus.PRE_USED)));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/13
     * @Description 堆存查询
     **/
    @AddLog
    @GetMapping(value = "/areaPosition")
    @ApiOperation(value = "堆存查询", notes = "堆存查询")
    public JsonResult areaPosition(@Valid AreaPositionsQuery query,
                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CStationAreaPositions> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq((StringUtils.isNotEmpty(query.getCode())), CStationAreaPositions::getCode, query.getCode()) // code全匹配查询
                .eq((StringUtils.isNotEmpty(query.getStationAreaCode())), CStationAreaPositions::getStationAreaCode, query.getStationAreaCode())
                .eq(CStationAreaPositions::getEnable, Status.ENABLED.getValue())
                .like((StringUtils.isNotEmpty(query.getContaNo())), CStationAreaPositions::getContaNo, query.getContaNo())
                .orderByDesc(CStationAreaPositions::getCreateTime);


        // 查询第1页，每页返回10条
        Page<CStationAreaPositions> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(query).orElseGet(AreaPositionsQuery::new))
                .set("useStatus", Lists.newArrayList(UseStatus.NOT_USED, UseStatus.USED, UseStatus.PRE_USED))
                .set("stationAreaId", areasService.select()));

    }


    /**
     * 下拉列表查询
     *
     * @return
     */
    @GetMapping(SELECT)
//    @Cacheable(value = "cDischarges", sync = true)
    @ApiOperation(value = "下拉列表查询", notes = "[List<CStationAreaPositions>],作者：ZhangPeng")
//    @JsonView(CStationAreaPositions.View.SELECT.class)
    public JsonResult select() {
        QueryWrapper<CStationAreaPositions> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CStationAreaPositions::getEnable, Status.ENABLED.getValue());
        List<CStationAreaPositions> list = service.list(wrapper);
        return JsonResult.data(list);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/6
     * @Description 下拉 集装箱列表（包括堆存堆位信息）
     **/
    @GetMapping("/selectContaOnly")
    @ApiOperation(value = "下拉 集装箱号，箱型", notes = "仅箱型箱号")
    public JsonResult selectContaOnly() {
        return JsonResult.data(service.selectConta());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/6
     * @Description 下拉 集装箱列表（包括堆存堆位信息）
     **/
    @GetMapping("/selectContano")
    @ApiOperation(value = "下拉 集装箱（堆存堆位）列表", notes = "List<AreaPositionContaSelect>")
    public JsonResult selectContano() {
        return JsonResult.data(service.selectAreaPositionConta());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/13
     * @Description
     **/
    @GetMapping("/selectUseable/{areaCode}")
    @ApiOperation(value = "下拉 选择可用", notes = "[List<CStationAreaPositions>]")
    @JsonView(CStationAreaPositions.View.SELECT.class)
    public JsonResult selectUseable(@PathVariable String areaCode) {
        return JsonResult.data(service.selectUseable(areaCode));
    }


    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 新增
     **/
    @GetMapping(value = NEW)
    @ApiOperation(value = "新增")
    @JsonView(CStationAreaPositions.View.Table.class)
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, CStationAreaPositions.builder().build())
        );
    }

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 查看
     **/
    @AddLog
//    @Cacheable(value = "cStationAreaPositions", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看")
    @JsonView(CStationAreaPositions.View.Table.class)
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 保存
     **/
    @AddLog
//    @CachePut(value = "cStationAreaPositions", key = "#cStationAreaPositions.id")
    @PostMapping
    @ApiOperation(value = "保存")
    @JsonView(CStationAreaPositions.View.Table.class)
    public JsonResult save(@RequestBody CStationAreaPositions cStationAreaPositions) throws Throwable {
        service.saveT(cStationAreaPositions);
        return JsonResult.success();
    }


    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 删除
     **/
    @AddLog
    @DeleteMapping(value = ID)
//    @CacheEvict(value = "cStationAreaPositions", key = "#id")
    @ApiOperation(value = "删除")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }


    @AddLog
    @PostMapping(value = "/checkContainer")
    @ApiOperation(value = "校验集装箱号和箱型", notes = "校验集装箱和箱型")
    public JsonResult checkContainer(@RequestParam String contaNo, @RequestParam String contaType) throws Throwable {
        service.checkContainer(contaNo, contaType);
        return JsonResult.success();
    }

}