/**
 * @filename:PBusinessTypeController 2019年7月17日
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
import cn.samples.depot.web.cz.service.PBusinessTypeService;
import cn.samples.depot.web.entity.PBusinessType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.PBusinessTypeController.API;

/**
 * @Description: 业务类型表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Api(tags = "数据字典-业务类型表", value = "业务类型表")
@RestController
@RequestMapping(value = API)
@Slf4j
public class PBusinessTypeController {

    static final String API = URL_PRE_STATION + "/PBusinessType";

    @Autowired
    private PBusinessTypeService service;

    /**
     * @return PageInfo<PBusinessType>
     * @explain 分页条件查询业务类型表
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @GetMapping
//    @Cacheable(value = "pBusinessType", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<PBusinessType>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<PBusinessType> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(baseQuery.getName())) {
            wrapper.lambda().like(PBusinessType::getName, baseQuery.getName());
        }
        if (null != baseQuery.getEnable()) {
            wrapper.lambda().eq(PBusinessType::getEnable, baseQuery.getEnable());
        }
        wrapper.lambda().orderByDesc(PBusinessType::getCreateTime);

        // 查询第1页，每页返回10条
        Page<PBusinessType> page = new Page<>(pageNum, pageSize);

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
    @ApiOperation(value = "下拉列表查询", notes = "[List<PBusinessType>],作者：chenjie")
//    @JsonView(PBusinessType.View.SELECT.class)
//    @Cacheable(value = "pBusinessType", sync = true)
    public JsonResult select() {
        QueryWrapper<PBusinessType> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PBusinessType::getEnable, Status.ENABLED.getValue());
        List<PBusinessType> list = service.list(wrapper);
        return JsonResult.data(list);
    }

    /**
     * Description: 查看指定业务类型表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月17日
     **/
    @AddLog
    @GetMapping(value = CODE)
//    @Cacheable(value = "pBusinessType", key = "#code")
    @ApiOperation("查看指定业务类型表数据")
    public JsonResult detail(@PathVariable String code) {
        return JsonResult.data(Params.param(M_DETAIL, service.getOne(Wrappers.<PBusinessType>lambdaQuery().eq(PBusinessType::getCode, code))));
    }

    /**
     * @return int
     * @explain 保存业务类型表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @PostMapping
//    @CachePut(value = "pBusinessType", key = "#pBusinessType.code")
    @ApiOperation(value = "保存业务类型表", notes = "保存业务类型表[pBusinessType],作者：ZhangPeng")
    public JsonResult save(@RequestBody PBusinessType pBusinessType) {
        return JsonResult.data(service.save(pBusinessType));
    }

    /**
     * @return int
     * @explain 单个删除业务类型表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @DeleteMapping(value = CODE)
    @ApiOperation(value = "单个删除业务类型表", notes = "单个删除业务类型表,作者：ZhangPeng")
//    @CacheEvict(value = "pBusinessType", key = "#code")
    public JsonResult deleteSingle(@PathVariable String code) {
        return JsonResult.data(service.remove(Wrappers.<PBusinessType>lambdaQuery().eq(PBusinessType::getCode, code)));
    }

    /**
     * @return int
     * @explain 批量删除业务类型表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @DeleteMapping
    @ApiOperation(value = "批量删除业务类型表", notes = "批量删除业务类型表,作者：ZhangPeng")
//    @CacheEvict(value = "pBusinessType", allEntries = true)
    public JsonResult deleteBatch(@RequestParam("codes") String... codes) {
        // 循环遍历删除
        Arrays.asList(codes).stream().forEach(code -> {
            service.remove(Wrappers.<PBusinessType>lambdaQuery().eq(PBusinessType::getCode, code));
        });

        return JsonResult.success();
    }

    /**
     * @return pBusinessType
     * @explain 更新业务类型表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @PutMapping(value = CODE)
    @ApiOperation(value = "更新业务类型表", notes = "更新业务类型表[pBusinessType],作者：ZhangPeng")
//    @CachePut(value = "pBusinessType", key = "#code")
    public JsonResult update(@PathVariable String code, @RequestBody PBusinessType pBusinessType) {
        return JsonResult.data(service.update(pBusinessType, Wrappers.<PBusinessType>lambdaQuery().eq(PBusinessType::getCode, code)));
    }

}