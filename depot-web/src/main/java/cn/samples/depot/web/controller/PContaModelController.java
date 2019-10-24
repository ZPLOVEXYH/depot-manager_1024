/**
 * @filename:PContaModelController 2019年7月18日
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
import cn.samples.depot.web.entity.PContaModel;
import cn.samples.depot.web.service.PContaModelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
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
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.controller.PContaModelController.API;

/**
 * @Description: 箱型表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月18日
 * @Version: V1.0
 */
@Api(tags = "数据字典-箱型管理", value = "箱型表")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class PContaModelController {

    static final String API = "/PContaModel";

    @Autowired
    private PContaModelService service;

    /**
     * @return PageInfo<PContaModel>
     * @explain 分页条件查询箱型表
     * @author ZhangPeng
     * @time 2019年7月18日
     */
    @AddLog
    @GetMapping
    @Cacheable(value = "pContaModel", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<PContaModel>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<PContaModel> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(baseQuery.getName())) {
            wrapper.lambda().like(PContaModel::getName, baseQuery.getName());
        }
        if (null != baseQuery.getEnable()) {
            wrapper.lambda().eq(PContaModel::getEnable, baseQuery.getEnable());
        }
        wrapper.lambda().orderByDesc(PContaModel::getCreateTime);

        // 查询第1页，每页返回10条
        Page<PContaModel> page = new Page<>(pageNum, pageSize);

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
    @ApiOperation(value = "下拉列表查询", notes = "[List<PContaModel>],作者：ZhangPeng")
    @JsonView(PContaModel.View.SELECT.class)
//    @Cacheable(value = "pContaModel", sync = true)
    public JsonResult select() {
        QueryWrapper<PContaModel> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PContaModel::getEnable, Status.ENABLED.getValue());
        List<PContaModel> list = service.list(wrapper);
        return JsonResult.data(list);
    }

    /**
     * Description: 查看指定箱型表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月18日
     **/
    @AddLog
    @GetMapping(value = CODE)
//    @Cacheable(value = "pContaModel", key = "#code")
    @ApiOperation("查看指定箱型表数据")
    public JsonResult detail(@PathVariable String code) {
        return JsonResult.data(Params.param(M_DETAIL, service.getOne(Wrappers.<PContaModel>lambdaQuery().eq(PContaModel::getCode, code))));
    }

    /**
     * @return int
     * @explain 保存箱型表对象
     * @author ZhangPeng
     * @time 2019年7月18日
     */
    @AddLog
    @PostMapping
    @CachePut(value = "pContaModel", key = "#pContaModel.code")
    @ApiOperation(value = "保存箱型表", notes = "保存箱型表[pContaModel],作者：ZhangPeng")
    public JsonResult save(@RequestBody PContaModel pContaModel) {
        return JsonResult.data(service.save(pContaModel));
    }

    /**
     * @return int
     * @explain 单个删除箱型表对象
     * @author ZhangPeng
     * @time 2019年7月18日
     */
    @AddLog
    @DeleteMapping(value = CODE)
    @CacheEvict(value = "pContaModel", key = "#code")
    @ApiOperation(value = "单个删除箱型表", notes = "单个删除箱型表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String code) {
        return JsonResult.data(service.remove(Wrappers.<PContaModel>lambdaQuery().eq(PContaModel::getCode, code)));
    }

    /**
     * @return int
     * @explain 批量删除箱型表对象
     * @author ZhangPeng
     * @time 2019年7月18日
     */
    @AddLog
    @DeleteMapping
    @CacheEvict(value = "pContaModel", allEntries = true)
    @ApiOperation(value = "批量删除箱型表", notes = "批量删除箱型表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("codes") String... codes) {
        // 循环遍历删除
        Arrays.asList(codes).stream().forEach(code -> {
            service.remove(Wrappers.<PContaModel>lambdaQuery().eq(PContaModel::getCode, code));
        });

        return JsonResult.success();
    }

    /**
     * @return pContaModel
     * @explain 更新箱型表对象
     * @author ZhangPeng
     * @time 2019年7月18日
     */
    @AddLog
    @PutMapping(value = CODE)
    @ApiOperation(value = "更新箱型表", notes = "更新箱型表[pContaModel],作者：ZhangPeng")
    @CachePut(value = "pContaModel", key = "#code")
    public JsonResult update(@PathVariable String code, @RequestBody PContaModel pContaModel) {
        return JsonResult.data(service.update(pContaModel, Wrappers.<PContaModel>lambdaQuery().eq(PContaModel::getCode, code)));
    }

}