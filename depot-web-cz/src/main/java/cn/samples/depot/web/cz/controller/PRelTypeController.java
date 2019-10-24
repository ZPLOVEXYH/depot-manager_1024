/**
 * @filename:PRelTypeController 2019年10月17日
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
import cn.samples.depot.web.cz.service.PRelTypeService;
import cn.samples.depot.web.entity.PRelType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.PRelTypeController.API;

/**
 * @Description: 放行方式表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年10月17日
 * @Version: V1.0
 */
@Api(tags = "数据字典-放行方式表", value = "放行方式表")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class PRelTypeController {

    static final String API = "/PRelType";

    @Autowired
    private PRelTypeService service;

    /**
     * @return PageInfo<PRelType>
     * @explain 分页条件查询放行方式表
     * @author ZhangPeng
     * @time 2019年10月17日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<PRelType>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<PRelType> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(baseQuery.getName())) {
            wrapper.lambda().like(PRelType::getName, baseQuery.getName());
        }
        if (StringUtils.isNotEmpty(baseQuery.getCode())) {
            wrapper.lambda().like(PRelType::getCode, baseQuery.getCode());
        }
        if (null != baseQuery.getEnable()) {
            wrapper.lambda().eq(PRelType::getEnable, baseQuery.getEnable());
        }
        wrapper.lambda().orderByDesc(PRelType::getCreateTime);

        // 查询第1页，每页返回10条
        Page<PRelType> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(baseQuery).orElseGet(BaseQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * Description: 查看指定放行方式表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年10月17日
     **/
    @AddLog
    @GetMapping(value = CODE)
    @ApiOperation("查看指定放行方式表数据")
    public JsonResult detail(@PathVariable String code) {
        return JsonResult.data(Params.param(M_DETAIL, service.getOne(Wrappers.<PRelType>lambdaQuery().eq(PRelType::getCode, code))));

    }

    /**
     * @return int
     * @explain 保存放行方式表对象
     * @author ZhangPeng
     * @time 2019年10月17日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存放行方式表", notes = "保存放行方式表[pRelType],作者：ZhangPeng")
    public JsonResult save(@RequestBody PRelType pRelType) {
        return JsonResult.data(service.save(pRelType));
    }

    /**
     * @return int
     * @explain 单个删除放行方式表对象
     * @author ZhangPeng
     * @time 2019年10月17日
     */
    @AddLog
    @DeleteMapping(value = CODE)
    @ApiOperation(value = "单个删除放行方式表", notes = "单个删除放行方式表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String code) {
        return JsonResult.data(service.remove(Wrappers.<PRelType>lambdaQuery().eq(PRelType::getCode, code)));
    }

    /**
     * @return int
     * @explain 批量删除放行方式表对象
     * @author ZhangPeng
     * @time 2019年10月17日
     */
    @AddLog
    @DeleteMapping
    @ApiOperation(value = "批量删除放行方式表", notes = "批量删除放行方式表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("codes") String... codes) {
        // 循环遍历删除
        Arrays.asList(codes).stream().forEach(code -> {
            service.remove(Wrappers.<PRelType>lambdaQuery().eq(PRelType::getCode, code));
        });

        return JsonResult.success();
    }

    /**
     * @return pRelType
     * @explain 更新放行方式表对象
     * @author ZhangPeng
     * @time 2019年10月17日
     */
    @AddLog
    @PutMapping(value = CODE)
    @ApiOperation(value = "更新放行方式表", notes = "更新放行方式表[pRelType],作者：ZhangPeng")
    public JsonResult update(@PathVariable String code, @RequestBody PRelType pRelType) {
        return JsonResult.data(service.update(pRelType, Wrappers.<PRelType>lambdaQuery().eq(PRelType::getCode, code)));
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增放行方式表", notes = "新增放行方式表[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param("detail", new PRelType()));
    }

}