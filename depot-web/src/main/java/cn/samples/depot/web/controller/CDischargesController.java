/**
 * @filename:CDischargesController 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.BaseQuery;
import cn.samples.depot.web.entity.CDischarges;
import cn.samples.depot.web.service.CDischargesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.controller.CDischargesController.API;

/**
 * @Description: 装卸货地表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Api(tags = "基础信息-装卸货地备案", value = "装卸货地表")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class CDischargesController {

    static final String API = "/CDischarges";

    @Autowired
    private CDischargesService service;

    /**
     * @return PageInfo<CDischarges>
     * @explain 分页条件查询装卸货地表
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @GetMapping
//    @Cacheable(value = "cDischarges", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<CDischarges>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CDischarges> wrapper = new QueryWrapper<>();

        wrapper.lambda().eq(baseQuery.getEnable() != null, CDischarges::getEnable, baseQuery.getEnable())
                .like(baseQuery.getName() != null, CDischarges::getName, baseQuery.getName())
                .eq(baseQuery.getCode() != null, CDischarges::getCode, baseQuery.getCode())
                .orderByDesc(CDischarges::getCreateTime);

        // 查询第1页，每页返回10条
        Page<CDischarges> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(baseQuery).orElseGet(BaseQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * 下拉列表查询
     *
     * @return
     */
    @GetMapping(SELECT)
    @ApiOperation(value = "下拉列表查询", notes = "返回对象[List<CDischarges>],作者：chenjie")
//    @JsonView(CDischarges.View.SELECT.class)
//    @Cacheable(value = "cDischarges", sync = true)
    public JsonResult select() {
        QueryWrapper<CDischarges> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CDischarges::getEnable, Status.ENABLED.getValue());
        return JsonResult.data(service.list(wrapper));
    }

    /**
     * Description: 查看指定装卸货地表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月18日
     **/
    @AddLog
//    @Cacheable(value = "cDischarges", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看指定装卸货地表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存装卸货地表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存装卸货地表", notes = "保存装卸货地表[cDischarges],作者：ZhangPeng")
//    @CachePut(value = "cDischarges", key = "#cDischarges.id")
    public JsonResult save(@RequestBody CDischarges cDischarges) {
        return JsonResult.data(service.save(cDischarges));
    }

    /**
     * @return int
     * @explain 单个删除装卸货地表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "单个删除装卸货地表", notes = "单个删除装卸货地表,作者：ZhangPeng")
//    @CacheEvict(value = "cDischarges", key = "#id")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除装卸货地表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @DeleteMapping
    @ApiOperation(value = "批量删除装卸货地表", notes = "批量删除装卸货地表,作者：ZhangPeng")
//    @CacheEvict(value = "cDischarges", allEntries = true)
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return cDischarges
     * @explain 更新装卸货地表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新装卸货地表", notes = "更新装卸货地表[cDischarges],作者：ZhangPeng")
//    @CachePut(value = "cDischarges", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody CDischarges cDischarges) {
        return JsonResult.data(service.update(cDischarges, Wrappers.<CDischarges>lambdaQuery().eq(CDischarges::getId, id)));
    }

}