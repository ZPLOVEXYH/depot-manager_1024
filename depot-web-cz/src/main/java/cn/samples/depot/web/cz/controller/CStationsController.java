/**
 * @filename:CStationsController 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.stations.StationsQuery;
import cn.samples.depot.web.cz.service.CStationsService;
import cn.samples.depot.web.entity.CStations;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.CStationsController.API;

/**
 * @Description: 场站表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Api(tags = "基础信息-堆场管理", value = "场站表")
@RestController
@RequestMapping(value = API)
@Slf4j
public class CStationsController {

    static final String API = URL_PRE_STATION + "/CStations";

    @Autowired
    private CStationsService service;

    /**
     * @return PageInfo<CStations>
     * @explain 分页条件查询场站表
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @GetMapping
//    @Cacheable(value = "cStations", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<CStations>],作者：ZhangPeng")
    public JsonResult index(@Valid StationsQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CStations> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq((query.getCode() != null && Strings.EMPTY != query.getCode()), CStations::getCode, query.getCode()) // code全匹配查询
                .like((query.getName() != null && Strings.EMPTY != query.getName()), CStations::getName, query.getName())
                .eq((query.getEnable() != null), CStations::getEnable, query.getEnable())
                .eq((query.getStationTypeCode() != null && Strings.EMPTY != query.getStationTypeCode()), CStations::getStationTypeCode, query.getStationTypeCode())
                .eq((query.getOperatorCode() != null && Strings.EMPTY != query.getOperatorCode()), CStations::getOperatorCode, query.getOperatorCode())
                .like((query.getOperatorName() != null && Strings.EMPTY != query.getOperatorName()), CStations::getOperatorName, query.getOperatorName())
                .like((query.getContact() != null && Strings.EMPTY != query.getContact()), CStations::getContact, query.getContact())
                .like((query.getContactPhone() != null && Strings.EMPTY != query.getContactPhone()), CStations::getContactPhone, query.getContactPhone())
                .like((query.getAddress() != null && Strings.EMPTY != query.getAddress()), CStations::getAddress, query.getAddress());


        // 查询第1页，每页返回10条
        Page<CStations> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(query).orElseGet(StationsQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * 下拉列表查询
     *
     * @return
     */
    @GetMapping(SELECT)
    @ApiOperation(value = "下拉列表查询", notes = "[List<CStations>],作者：ZhangPeng")
//    @Cacheable(value = "cStations", sync = true)
//    @JsonView(CStations.View.SELECT.class)
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
//    @Cacheable(value = "cStations", key = "#id")
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
//    @CachePut(value = "cStations", key = "#cStations.id")
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
//    @CacheEvict(value = "cStations", key = "#id")
    @ApiOperation(value = "单个删除场站表", notes = "单个删除场站表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除场站表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @DeleteMapping
//    @CacheEvict(value = "cStations", allEntries = true)
    @ApiOperation(value = "批量删除场站表", notes = "批量删除场站表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
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
//    @CachePut(value = "cStations", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody CStations cStations) {
        return JsonResult.data(service.update(cStations, Wrappers.<CStations>lambdaQuery().eq(CStations::getId, id)));
    }

}