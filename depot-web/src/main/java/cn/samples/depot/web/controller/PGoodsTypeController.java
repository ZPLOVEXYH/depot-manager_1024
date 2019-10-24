/**
 * @filename:PGoodsTypeController 2019年7月17日
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
import cn.samples.depot.web.entity.PGoodsType;
import cn.samples.depot.web.service.PGoodsTypeService;
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
import static cn.samples.depot.web.controller.PGoodsTypeController.API;

/**
 * @Description: 货物类型表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Api(tags = "数据字典-货物类型表", value = "货物类型表")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class PGoodsTypeController {

    static final String API = "/PGoodsType";

    @Autowired
    private PGoodsTypeService service;

    /**
     * @return PageInfo<PGoodsType>
     * @explain 分页条件查询货物类型表
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @GetMapping
//    @Cacheable(value = "pGoodsType", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<PGoodsType>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<PGoodsType> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(baseQuery.getName())) {
            wrapper.lambda().like(PGoodsType::getName, baseQuery.getName());
        }
        if (null != baseQuery.getEnable()) {
            wrapper.lambda().eq(PGoodsType::getEnable, baseQuery.getEnable());
        }
        wrapper.lambda().orderByDesc(PGoodsType::getCreateTime);

        // 查询第1页，每页返回10条
        Page<PGoodsType> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(baseQuery).orElseGet(BaseQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }


    /**
     * Description: 查看货物类型表查询
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月23日
     **/
    @GetMapping(SELECT)
    @ApiOperation(value = "货物类型表查询", notes = "返回货物类型表列表,作者：ZhangPeng")
//    @JsonView(PGoodsType.View.SELECT.class)
//    @Cacheable(value = "pGoodsType", sync = true)
    public JsonResult select() {
        QueryWrapper<PGoodsType> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PGoodsType::getEnable, Status.ENABLED.getValue());
        List<PGoodsType> list = service.list(wrapper);
        return JsonResult.data(list);
    }

    /**
     * Description: 查看指定货物类型表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月17日
     **/
    @AddLog
    @GetMapping(value = CODE)
//    @Cacheable(value = "pGoodsType", key = "#code")
    @ApiOperation("查看指定货物类型表数据")
    public JsonResult detail(@PathVariable String code) {
        return JsonResult.data(Params.param(M_DETAIL, service.getOne(Wrappers.<PGoodsType>lambdaQuery().eq(PGoodsType::getCode, code))));
    }

    /**
     * @return int
     * @explain 保存货物类型表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @PostMapping
//    @CachePut(value = "pGoodsType", key = "#pGoodsType.code")
    @ApiOperation(value = "保存货物类型表", notes = "保存货物类型表[pGoodsType],作者：ZhangPeng")
    public JsonResult save(@RequestBody PGoodsType pGoodsType) {
        return JsonResult.data(service.save(pGoodsType));
    }

    /**
     * @return int
     * @explain 单个删除货物类型表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @DeleteMapping(value = CODE)
//    @CacheEvict(value = "pGoodsType", key = "#code")
    @ApiOperation(value = "单个删除货物类型表", notes = "单个删除货物类型表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String code) {
        return JsonResult.data(service.remove(Wrappers.<PGoodsType>lambdaQuery().eq(PGoodsType::getCode, code)));
    }

    /**
     * @return int
     * @explain 批量删除货物类型表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @DeleteMapping
//    @CacheEvict(value = "pGoodsType", allEntries = true)
    @ApiOperation(value = "批量删除货物类型表", notes = "批量删除货物类型表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("codes") String... codes) {
        // 循环遍历删除
        Arrays.asList(codes).stream().forEach(code -> {
            service.remove(Wrappers.<PGoodsType>lambdaQuery().eq(PGoodsType::getCode, code));
        });

        return JsonResult.success();
    }

    /**
     * @return pGoodsType
     * @explain 更新货物类型表对象
     * @author ZhangPeng
     * @time 2019年7月17日
     */
    @AddLog
    @PutMapping(value = CODE)
    @ApiOperation(value = "更新货物类型表", notes = "更新货物类型表[pGoodsType],作者：ZhangPeng")
//    @CachePut(value = "pGoodsType", key = "#code")
    public JsonResult update(@PathVariable String code, @RequestBody PGoodsType pGoodsType) {
        return JsonResult.data(service.update(pGoodsType, Wrappers.<PGoodsType>lambdaQuery().eq(PGoodsType::getCode, code)));
    }

}