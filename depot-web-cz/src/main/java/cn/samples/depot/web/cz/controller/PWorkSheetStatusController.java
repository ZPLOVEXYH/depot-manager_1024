/**
 * @filename:PWorkSheetStatusController 2019年7月17日
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
import cn.samples.depot.web.cz.service.PWorkSheetStatusService;
import cn.samples.depot.web.entity.PWorkSheetStatus;
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
import static cn.samples.depot.web.cz.controller.PWorkSheetStatusController.API;

/**
 * @Description: 作业单状态表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Api(tags = "数据字典-作业单状态表", value = "作业单状态表")
@RestController
@RequestMapping(value = API)
@Slf4j
public class PWorkSheetStatusController {

    static final String API = URL_PRE_STATION + "/PWorkSheetStatus";

    @Autowired
    private PWorkSheetStatusService service;

    /**
     * @return PageInfo<PWorkSheetStatus>
     * @explain 分页条件查询作业单状态表
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<PWorkSheetStatus>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<PWorkSheetStatus> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(baseQuery.getName())) {
            wrapper.lambda().like(PWorkSheetStatus::getName, baseQuery.getName());
        }
        if (null != baseQuery.getEnable()) {
            wrapper.lambda().eq(PWorkSheetStatus::getEnable, baseQuery.getEnable());
        }
        wrapper.lambda().orderByDesc(PWorkSheetStatus::getCreateTime);

        // 查询第1页，每页返回10条
        Page<PWorkSheetStatus> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(baseQuery).orElseGet(BaseQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }


    /**
     * Description: 查看作业单状态表查询
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月23日
     **/
    @GetMapping(SELECT)
    @ApiOperation(value = "作业单状态表查询", notes = "返回作业单状态表列表,作者：ZhangPeng")
//    @JsonView(PWorkSheetStatus.View.SELECT.class)
//    @Cacheable(value = "pWorkSheetStatus", sync = true)
    public JsonResult select() {
        QueryWrapper<PWorkSheetStatus> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PWorkSheetStatus::getEnable, Status.ENABLED.getValue());
        List<PWorkSheetStatus> list = service.list(wrapper);
        return JsonResult.data(list);
    }

    /**
     * Description: 查看指定作业单状态表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月17日
     **/
    @AddLog
    @GetMapping(value = CODE)
//    @Cacheable(value = "pWorkSheetStatus", key = "#code")
    @ApiOperation("查看指定作业单状态表数据")
    public JsonResult detail(@PathVariable String code) {
        return JsonResult.data(Params.param(M_DETAIL, service.getOne(Wrappers.<PWorkSheetStatus>lambdaQuery().eq(PWorkSheetStatus::getCode, code))));
    }

    /**
     * @return int
     * @explain 保存作业单状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @PostMapping
//    @CachePut(value = "pWorkSheetStatus", key = "#pWorkSheetStatus.code")
    @ApiOperation(value = "保存作业单状态表", notes = "保存作业单状态表[pWorkSheetStatus],作者：ZhangPeng")
    public JsonResult save(@RequestBody PWorkSheetStatus pWorkSheetStatus) {
        return JsonResult.data(service.save(pWorkSheetStatus));
    }

    /**
     * @return int
     * @explain 单个删除作业单状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @DeleteMapping(value = CODE)
//    @CacheEvict(value = "pWorkSheetStatus", key = "#code")
    @ApiOperation(value = "单个删除作业单状态表", notes = "单个删除作业单状态表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String code) {
        return JsonResult.data(service.remove(Wrappers.<PWorkSheetStatus>lambdaQuery().eq(PWorkSheetStatus::getCode, code)));
    }

    /**
     * @return int
     * @explain 批量删除作业单状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @DeleteMapping
//    @CacheEvict(value = "pWorkSheetStatus", allEntries = true)
    @ApiOperation(value = "批量删除作业单状态表", notes = "批量删除作业单状态表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("codes") String... codes) {
        // 循环遍历删除
        Arrays.asList(codes).stream().forEach(code -> {
            service.remove(Wrappers.<PWorkSheetStatus>lambdaQuery().eq(PWorkSheetStatus::getCode, code));
        });

        return JsonResult.success();
    }

    /**
     * @return pWorkSheetStatus
     * @explain 更新作业单状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @PutMapping(value = CODE)
    @ApiOperation(value = "更新作业单状态表", notes = "更新作业单状态表[pWorkSheetStatus],作者：ZhangPeng")
//    @CachePut(value = "pWorkSheetStatus", key = "#code")
    public JsonResult update(@PathVariable String code, @RequestBody PWorkSheetStatus pWorkSheetStatus) {
        return JsonResult.data(service.update(pWorkSheetStatus, Wrappers.<PWorkSheetStatus>lambdaQuery().eq(PWorkSheetStatus::getCode, code)));
    }

}