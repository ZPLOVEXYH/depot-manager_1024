package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.stations.area.StationAreasQuery;
import cn.samples.depot.web.cz.service.CStationAreasService;
import cn.samples.depot.web.entity.CStationAreas;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.CStationAreasController.API;


/**
 * @Author majunzi
 * @Date 2019/10/14
 * @Description 堆区管理
 **/
@Api(tags = "基础信息-堆区管理", value = "堆区表")
@RestController
@RequestMapping(value = API)
@Slf4j
public class CStationAreasController {

    static final String API = URL_PRE_STATION + "/CStationAreas";

    @Autowired
    private CStationAreasService service;


    /**
     * @Author majunzi
     * @Date 2019/10/14
     * @Description
     **/
    @AddLog
    @GetMapping
//    @Cacheable(value = "cStationAreas", sync = true)
    @ApiOperation(value = "查询")
    public JsonResult index(@Valid StationAreasQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CStationAreas> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq((query.getCode() != null && Strings.EMPTY != query.getCode()), CStationAreas::getCode, query.getCode()) // code全匹配查询
                .ge((query.getStartCreateTime() != null && query.getStartCreateTime() > 0), CStationAreas::getCreateTime, query.getStartCreateTime())
                .le((query.getEndCreateTime() != null && query.getEndCreateTime() > 0), CStationAreas::getCreateTime, query.getEndCreateTime())
                .orderByDesc(CStationAreas::getCreateTime);


        // 查询第1页，每页返回10条
        Page<CStationAreas> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(query).orElseGet(StationAreasQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * 下拉列表查询
     *
     * @return
     */
    @GetMapping(SELECT)
//    @Cacheable(value = "cStationAreas", sync = true)
    @ApiOperation(value = "下拉列表查询")
    public JsonResult select() {
        return JsonResult.data(service.select());

    }

    /**
     * @Author majunzi
     * @Date 2019/8/6
     * @Description 下拉列表 选择可用
     **/
    @GetMapping("/selectUseable")
//    @Cacheable(value = "cStationAreas", sync = true)
    @ApiOperation(value = "下拉列表 选择可用")
    @JsonView(CStationAreas.View.SELECT.class)
    public JsonResult selectUseable() {
        return JsonResult.data(service.selectUseable());

    }


    /**
     * @Author majunzi
     * @Date 2019/10/14
     * @Description 查看
     **/
    @AddLog
//    @Cacheable(value = "cStationAreas", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }


    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 新增
     **/
    @GetMapping(value = NEW)
    @ApiOperation(value = "新增")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, CStationAreas.builder().build())
        );
    }

    /**
     * @Author majunzi
     * @Date 2019/10/14
     * @Description 保存
     **/
    @AddLog
//    @CachePut(value = "cStationAreas", key = "#cStationAreas.id")
    @PostMapping
    @ApiOperation(value = "保存")
    public JsonResult save(@RequestBody CStationAreas cStationAreas) throws Throwable {
        service.saveT(cStationAreas);
        return JsonResult.success();
    }


    /**
     * @Author majunzi
     * @Date 2019/10/14
     * @Description 删除
     **/
    @AddLog
    @DeleteMapping(value = ID)
//    @CacheEvict(value = "cStationAreas", key = "#id")
    @ApiOperation(value = "删除")
    public JsonResult deleteSingle(@PathVariable String id) throws Throwable {
        service.delete(id);
        return JsonResult.success();
    }


    /**
     * @Author majunzi
     * @Date 2019/10/14
     * @Description
     **/
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新")
//    @CachePut(value = "cStationAreas", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody CStationAreas cStationAreas) throws Throwable {
        service.updateT(id, cStationAreas);
        return JsonResult.success();
    }


}