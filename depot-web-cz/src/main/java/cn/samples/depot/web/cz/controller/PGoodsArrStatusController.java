/**
 * @filename:PGoodsArrStatusController 2019年7月17日
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
import cn.samples.depot.web.cz.service.PGoodsArrStatusService;
import cn.samples.depot.web.entity.PGoodsArrStatus;
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
import static cn.samples.depot.web.cz.controller.PGoodsArrStatusController.API;

/**
 * @Description: 到货状态表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Api(tags = "数据字典-到货状态表", value = "到货状态表")
@RestController
@RequestMapping(value = API)
@Slf4j
public class PGoodsArrStatusController {

    static final String API = URL_PRE_STATION + "/PGoodsArrStatus";

    @Autowired
    private PGoodsArrStatusService service;

    /**
     * @return PageInfo<PGoodsArrStatus>
     * @explain 分页条件查询到货状态表
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @GetMapping
//    @Cacheable(value = "pGoodsArrStatus", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<PGoodsArrStatus>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<PGoodsArrStatus> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(baseQuery.getName())) {
            wrapper.lambda().like(PGoodsArrStatus::getName, baseQuery.getName());
        }
        if (null != baseQuery.getEnable()) {
            wrapper.lambda().eq(PGoodsArrStatus::getEnable, baseQuery.getEnable());
        }
        wrapper.lambda().orderByDesc(PGoodsArrStatus::getCreateTime);

        // 查询第1页，每页返回10条
        Page<PGoodsArrStatus> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(baseQuery).orElseGet(BaseQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * Description: 查看到货状态表查询
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月23日
     **/
    @GetMapping(SELECT)
    @ApiOperation(value = "到货状态表查询", notes = "返回到货状态表列表,作者：ZhangPeng")
//    @JsonView(PGoodsArrStatus.View.SELECT.class)
//    @Cacheable(value = "pGoodsArrStatus", sync = true)
    public JsonResult select() {
        QueryWrapper<PGoodsArrStatus> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PGoodsArrStatus::getEnable, Status.ENABLED.getValue());
        List<PGoodsArrStatus> list = service.list(wrapper);
        return JsonResult.data(list);
    }

    /**
     * Description: 查看指定到货状态表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月17日
     **/
    @AddLog
    @GetMapping(value = CODE)
//    @Cacheable(value = "pGoodsArrStatus", key = "#code")
    @ApiOperation("查看指定到货状态表数据")
    public JsonResult detail(@PathVariable String code) {
        return JsonResult.data(Params.param(M_DETAIL, service.getOne(Wrappers.<PGoodsArrStatus>lambdaQuery().eq(PGoodsArrStatus::getCode, code))));
    }

    /**
     * @return int
     * @explain 保存到货状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @PostMapping
//    @CachePut(value = "pGoodsArrStatus", key = "#pGoodsArrStatus.code")
    @ApiOperation(value = "保存到货状态表", notes = "保存到货状态表[pGoodsArrStatus],作者：ZhangPeng")
    public JsonResult save(@RequestBody PGoodsArrStatus pGoodsArrStatus) {
        return JsonResult.data(service.save(pGoodsArrStatus));
    }

    /**
     * @return int
     * @explain 单个删除到货状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @DeleteMapping(value = CODE)
//    @CacheEvict(value = "pGoodsArrStatus", key = "#code")
    @ApiOperation(value = "单个删除到货状态表", notes = "单个删除到货状态表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String code) {
        return JsonResult.data(service.remove(Wrappers.<PGoodsArrStatus>lambdaQuery().eq(PGoodsArrStatus::getCode, code)));
    }

    /**
     * @return int
     * @explain 批量删除到货状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @DeleteMapping
//    @CacheEvict(value = "pGoodsArrStatus", allEntries = true)
    @ApiOperation(value = "批量删除到货状态表", notes = "批量删除到货状态表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("codes") String... codes) {
        // 循环遍历删除
        Arrays.asList(codes).stream().forEach(code -> {
            service.remove(Wrappers.<PGoodsArrStatus>lambdaQuery().eq(PGoodsArrStatus::getCode, code));
        });

        return JsonResult.success();
    }

    /**
     * @return pGoodsArrStatus
     * @explain 更新到货状态表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @PutMapping(value = CODE)
    @ApiOperation(value = "更新到货状态表", notes = "更新到货状态表[pGoodsArrStatus],作者：ZhangPeng")
//    @CachePut(value = "pGoodsArrStatus", key = "#code")
    public JsonResult update(@PathVariable String code, @RequestBody PGoodsArrStatus pGoodsArrStatus) {
        return JsonResult.data(service.update(pGoodsArrStatus, Wrappers.<PGoodsArrStatus>lambdaQuery().eq(PGoodsArrStatus::getCode, code)));
    }

}