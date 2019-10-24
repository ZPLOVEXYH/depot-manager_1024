/**
 * @filename:PTransportTypeController 2019年7月19日
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
import cn.samples.depot.web.entity.PTransportType;
import cn.samples.depot.web.service.PTransportTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.controller.PTransportTypeController.API;

/**
 * @Description: 运输方式类型表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Api(tags = "数据字典-运输方式类型表", value = "运输方式类型表")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class PTransportTypeController {

    static final String API = "/PTransportType";

    @Autowired
    private PTransportTypeService service;

    /**
     * @return PageInfo<PTransportType>
     * @explain 分页条件查询运输方式类型表
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @GetMapping
//    @Cacheable(value = "pTransportType", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<PTransportType>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<PTransportType> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(baseQuery.getName())) {
            wrapper.lambda().like(PTransportType::getName, baseQuery.getName());
        }
        if (null != baseQuery.getEnable()) {
            wrapper.lambda().eq(PTransportType::getEnable, baseQuery.getEnable());
        }
        wrapper.lambda().orderByDesc(PTransportType::getCreateTime);

        // 查询第1页，每页返回10条
        Page<PTransportType> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(baseQuery).orElseGet(BaseQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }


    /**
     * Description: 查看运输方式类型表查询
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月23日
     **/
    @GetMapping(SELECT)
    @ApiOperation(value = "运输方式类型表查询", notes = "返回运输方式类型表列表,作者：ZhangPeng")
//    @JsonView(PTransportType.View.SELECT.class)
//    @Cacheable(value = "pTransportType", sync = true)
    public JsonResult select() {
        QueryWrapper<PTransportType> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PTransportType::getEnable, Status.ENABLED.getValue());
        List<PTransportType> list = service.list(wrapper);
        return JsonResult.data(list);
    }

    /**
     * Description: 查看指定运输方式类型表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月19日
     **/
    @AddLog
    @GetMapping(value = CODE)
//    @Cacheable(value = "pTransportType", key = "#code")
    @ApiOperation("查看指定运输方式类型表数据")
    public JsonResult detail(@PathVariable String code) {
        return JsonResult.data(Params.param(M_DETAIL, service.getOne(Wrappers.<PTransportType>lambdaQuery().eq(PTransportType::getCode, code))));
    }

    /**
     * @return int
     * @explain 保存运输方式类型表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @PostMapping
    @CachePut(value = "pTransportType", key = "#pTransportType.code")
    @ApiOperation(value = "保存运输方式类型表", notes = "保存运输方式类型表[pTransportType],作者：ZhangPeng")
    public JsonResult save(@RequestBody PTransportType pTransportType) {
        return JsonResult.data(service.save(pTransportType));
    }

    /**
     * @return int
     * @explain 单个删除运输方式类型表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @DeleteMapping(value = CODE)
    @CacheEvict(value = "pTransportType", key = "#code")
    @ApiOperation(value = "单个删除运输方式类型表", notes = "单个删除运输方式类型表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String code) {
        return JsonResult.data(service.remove(Wrappers.<PTransportType>lambdaQuery().eq(PTransportType::getCode, code)));
    }

    /**
     * @return int
     * @explain 批量删除运输方式类型表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @DeleteMapping
    @CacheEvict(value = "pTransportType", allEntries = true)
    @ApiOperation(value = "批量删除运输方式类型表", notes = "批量删除运输方式类型表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("codes") String... codes) {
        // 循环遍历删除
        Arrays.asList(codes).stream().forEach(code -> {
            service.remove(Wrappers.<PTransportType>lambdaQuery().eq(PTransportType::getCode, code));
        });

        return JsonResult.success();
    }

    /**
     * @return pTransportType
     * @explain 更新运输方式类型表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @PutMapping(value = CODE)
    @ApiOperation(value = "更新运输方式类型表", notes = "更新运输方式类型表[pTransportType],作者：ZhangPeng")
    @CachePut(value = "pTransportType", key = "#code")
    public JsonResult update(@PathVariable String code, @RequestBody PTransportType pTransportType) {
        return JsonResult.data(service.update(pTransportType, Wrappers.<PTransportType>lambdaQuery().eq(PTransportType::getCode, code)));
    }

}