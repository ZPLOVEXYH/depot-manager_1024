/**
 * @filename:PAreaCodeController 2019年7月18日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.BaseQuery;
import cn.samples.depot.web.cz.service.PAreaCodeService;
import cn.samples.depot.web.entity.PAreaCode;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.PAreaCodeController.API;

/**
 * @Description: 监管区域表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月18日
 * @Version: V1.0
 */
@Api(tags = "基础信息-监管区域", value = "监管区域表")
@RestController
@RequestMapping(value = API)
@Slf4j
public class PAreaCodeController {

    static final String API = URL_PRE_STATION + "/PAreaCode";

    @Autowired
    private PAreaCodeService service;

    /**
     * @return PageInfo<PAreaCode>
     * @explain 分页条件查询监管区域表
     * @author ZhangPeng
     * @time 2019年7月18日
     */
    @AddLog
    @GetMapping
    @Cacheable(value = "pAreaCode", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<PAreaCode>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<PAreaCode> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(baseQuery.getName())) {
            wrapper.lambda().like(PAreaCode::getName, baseQuery.getName());
        }
        if (null != baseQuery.getEnable()) {
            wrapper.lambda().eq(PAreaCode::getEnable, baseQuery.getEnable());
        }
        wrapper.lambda().orderByDesc(PAreaCode::getCreateTime);

        // 查询第1页，每页返回10条
        Page<PAreaCode> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(baseQuery).orElseGet(BaseQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * 下拉查询
     *
     * @return
     */
    @GetMapping(SELECT)
    @ApiOperation(value = "下拉查询", notes = "返回[List<PAreaCode>],作者：ZhangPeng")
//    @Cacheable(value = "pAreaCode", sync = true)
    public JsonResult select() {
        QueryWrapper<PAreaCode> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PAreaCode::getEnable, Status.ENABLED.getValue());
        return JsonResult.data(service.list(wrapper));
    }

    /**
     * Description: 查看指定监管区域表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月18日
     **/
    @AddLog
    @GetMapping(value = CODE)
//    @Cacheable(value = "pAreaCode", key = "#code")
    @ApiOperation("查看指定监管区域表数据")
    public JsonResult detail(@PathVariable String code) {
        return JsonResult.data(Params.param(M_DETAIL, service.getOne(Wrappers.<PAreaCode>lambdaQuery().eq(PAreaCode::getCode, code))));
    }

    /**
     * @return int
     * @explain 保存监管区域表对象
     * @author ZhangPeng
     * @time 2019年7月18日
     */
    @AddLog
    @PostMapping
    @CachePut(value = "pAreaCode", key = "#pAreaCode.code")
    @ApiOperation(value = "保存监管区域表", notes = "保存监管区域表[pAreaCode],作者：ZhangPeng")
    public JsonResult save(@RequestBody PAreaCode pAreaCode) {
        return JsonResult.data(service.save(pAreaCode));
    }

    /**
     * @return int
     * @explain 单个删除监管区域表对象
     * @author ZhangPeng
     * @time 2019年7月18日
     */
    @AddLog
    @DeleteMapping(value = CODE)
    @CacheEvict(value = "pAreaCode", key = "#code")
    @ApiOperation(value = "单个删除监管区域表", notes = "单个删除监管区域表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String code) {
        return JsonResult.data(service.remove(Wrappers.<PAreaCode>lambdaQuery().eq(PAreaCode::getCode, code)));
    }

    /**
     * @return int
     * @explain 批量删除监管区域表对象
     * @author ZhangPeng
     * @time 2019年7月18日
     */
    @AddLog
    @DeleteMapping
    @CacheEvict(value = "pAreaCode", allEntries = true)
    @ApiOperation(value = "批量删除监管区域表", notes = "批量删除监管区域表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("codes") String... codes) {
        // 循环遍历删除
        Arrays.asList(codes).stream().forEach(code -> {
            service.remove(Wrappers.<PAreaCode>lambdaQuery().eq(PAreaCode::getCode, code));
        });

        return JsonResult.success();
    }

    /**
     * @return pAreaCode
     * @explain 更新监管区域表对象
     * @author ZhangPeng
     * @time 2019年7月18日
     */
    @PutMapping(value = CODE)
    @ApiOperation(value = "更新监管区域表", notes = "更新监管区域表[pAreaCode],作者：ZhangPeng")
    @CachePut(value = "pAreaCode", key = "#code")
    public JsonResult update(@PathVariable String code, @RequestBody PAreaCode pAreaCode) {
        return JsonResult.data(service.update(pAreaCode, Wrappers.<PAreaCode>lambdaQuery().eq(PAreaCode::getCode, code)));
    }

}