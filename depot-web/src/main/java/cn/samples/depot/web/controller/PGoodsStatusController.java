/**
 * @filename:PGoodsStatusController 2019年7月17日
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
import cn.samples.depot.web.entity.PGoodsStatus;
import cn.samples.depot.web.service.PGoodsStatusService;
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
import static cn.samples.depot.web.controller.PGoodsStatusController.API;

/**
 * @Description: 货物状态表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Api(tags = "数据字典-货物状态表", value = "货物状态表")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class PGoodsStatusController {

    static final String API = "/PGoodsStatus";

    @Autowired
    private PGoodsStatusService service;

    /**
     * @return PageInfo<PGoodsStatus>
     * @explain 分页条件查询货物状态表
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @GetMapping
//    @Cacheable(value = "pGoodsStatus", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<PGoodsStatus>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<PGoodsStatus> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(baseQuery.getName())) {
            wrapper.lambda().like(PGoodsStatus::getName, baseQuery.getName());
        }
        if (null != baseQuery.getEnable()) {
            wrapper.lambda().eq(PGoodsStatus::getEnable, baseQuery.getEnable());
        }
        wrapper.lambda().orderByDesc(PGoodsStatus::getCreateTime);

        // 查询第1页，每页返回10条
        Page<PGoodsStatus> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(baseQuery).orElseGet(BaseQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * Description: 查看货物状态表查询
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月23日
     **/
    @GetMapping(SELECT)
    @ApiOperation(value = "货物状态表查询", notes = "返回货物状态表列表,作者：ZhangPeng")
//    @JsonView(PGoodsStatus.View.SELECT.class)
//    @Cacheable(value = "pGoodsStatus", sync = true)
    public JsonResult select() {
        QueryWrapper<PGoodsStatus> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PGoodsStatus::getEnable, Status.ENABLED.getValue());
        List<PGoodsStatus> list = service.list(wrapper);
        return JsonResult.data(list);
    }

    /**
     * Description: 查看指定货物状态表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月17日
     **/
    @AddLog
    @GetMapping(value = CODE)
//    @Cacheable(value = "pGoodsStatus", key = "#code")
    @ApiOperation("查看指定货物状态表数据")
    public JsonResult detail(@PathVariable String code) {
        return JsonResult.data(Params.param(M_DETAIL, service.getOne(Wrappers.<PGoodsStatus>lambdaQuery().eq(PGoodsStatus::getCode, code))));
    }

    /**
     * @return int
     * @explain 保存货物状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @PostMapping
//    @CachePut(value = "pGoodsStatus", key = "#pGoodsStatus.code")
    @ApiOperation(value = "保存货物状态表", notes = "保存货物状态表[pGoodsStatus],作者：ZhangPeng")
    public JsonResult save(@RequestBody PGoodsStatus pGoodsStatus) {
        return JsonResult.data(service.save(pGoodsStatus));
    }

    /**
     * @return int
     * @explain 单个删除货物状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @DeleteMapping(value = CODE)
//    @CacheEvict(value = "pGoodsStatus", key = "#code")
    @ApiOperation(value = "单个删除货物状态表", notes = "单个删除货物状态表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String code) {
        return JsonResult.data(service.remove(Wrappers.<PGoodsStatus>lambdaQuery().eq(PGoodsStatus::getCode, code)));
    }

    /**
     * @return int
     * @explain 批量删除货物状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @DeleteMapping
//    @CacheEvict(value = "pGoodsStatus", allEntries = true)
    @ApiOperation(value = "批量删除货物状态表", notes = "批量删除货物状态表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("codes") String... codes) {
        // 循环遍历删除
        Arrays.asList(codes).stream().forEach(code -> {
            service.remove(Wrappers.<PGoodsStatus>lambdaQuery().eq(PGoodsStatus::getCode, code));
        });

        return JsonResult.success();
    }

    /**
     * @return pGoodsStatus
     * @explain 更新货物状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @PutMapping(value = CODE)
    @ApiOperation(value = "更新货物状态表", notes = "更新货物状态表[pGoodsStatus],作者：ZhangPeng")
//    @CachePut(value = "pGoodsStatus", key = "#code")
    public JsonResult update(@PathVariable String code, @RequestBody PGoodsStatus pGoodsStatus) {
        return JsonResult.data(service.update(pGoodsStatus, Wrappers.<PGoodsStatus>lambdaQuery().eq(PGoodsStatus::getCode, code)));
    }

}