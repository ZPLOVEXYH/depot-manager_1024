/**
 * @filename:CStationsController 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.entity.CStations;
import cn.samples.depot.web.service.CStationsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.controller.CStationsController.API;

/**
 * @Description: 场站表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Api(tags = "基础信息-场站备案", value = "场站表")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class CStationsController {

    static final String API = "/CStations";

    @Autowired
    private CStationsService service;

    /**
     * 下拉列表查询
     *
     * @return
     */
    @GetMapping(SELECT)
    @ApiOperation(value = "下拉列表查询", notes = "[List<CStations>],作者：ZhangPeng")
    @JsonView(CStations.View.SELECT.class)
    public JsonResult select() {
        QueryWrapper<CStations> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CStations::getEnable, Status.ENABLED.getValue());
        List<CStations> list = service.list(wrapper);
        return JsonResult.data(list);
    }

    /**
     * Description: 查看指定场站表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月18日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看指定场站表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存场站表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存场站表", notes = "保存场站表[cStations],作者：ZhangPeng")
    public JsonResult save(@RequestBody CStations cStations) {
        return JsonResult.data(service.save(cStations));
    }

    @AddLog
    @GetMapping("/me")
    @ApiOperation(value = "获取当前场站信息", notes = "场站表[cStations],作者：ChenJie")
    public JsonResult me() {
        CStations cStations = service.getOne(null);
        return JsonResult.data(Params.param(M_DETAIL, cStations));
    }

    /**
     * @return int
     * @explain 单个删除场站表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "单个删除场站表", notes = "单个删除场站表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }


    /**
     * @return cStations
     * @explain 更新场站表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新场站表", notes = "更新场站表[cStations],作者：ZhangPeng")
    public JsonResult update(@PathVariable String id, @RequestBody CStations cStations) {
        return JsonResult.data(service.update(cStations, Wrappers.<CStations>lambdaQuery().eq(CStations::getId, id)));
    }

}