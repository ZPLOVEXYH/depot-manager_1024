/**
 * @filename:PCustomsCodeController 2019年7月19日
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
import cn.samples.depot.web.cz.service.PCustomsCodeService;
import cn.samples.depot.web.entity.PCustomsCode;
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
import static cn.samples.depot.web.cz.controller.PCustomsCodeController.API;

/**
 * @Description: 海关代码表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Api(tags = "基础信息-海关代码", value = "海关代码表")
@RestController
@RequestMapping(value = API)
@Slf4j
public class PCustomsCodeController {

    static final String API = URL_PRE_STATION + "/PCustomsCode";

    @Autowired
    private PCustomsCodeService service;

    /**
     * @return PageInfo<PCustomsCode>
     * @explain 分页条件查询海关代码表
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<PCustomsCode>],作者：ZhangPeng")
    public JsonResult index(@Valid BaseQuery baseQuery,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<PCustomsCode> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(baseQuery.getName())) {
            wrapper.lambda().like(PCustomsCode::getName, baseQuery.getName());
        }
        if (null != baseQuery.getEnable()) {
            wrapper.lambda().eq(PCustomsCode::getEnable, baseQuery.getEnable());
        }
        wrapper.lambda().orderByDesc(PCustomsCode::getCreateTime);

        // 查询第1页，每页返回10条
        Page<PCustomsCode> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(baseQuery).orElseGet(BaseQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }


    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 下拉列表，仅code+name
     **/
    @GetMapping(SELECT)
    @ApiOperation(value = "下拉列表，仅code+name")
    public JsonResult select() {
        return JsonResult.data(service.select());
    }

    /**
     * Description: 查看指定海关代码表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月19日
     **/
    @AddLog
    @GetMapping(value = CODE)
    @ApiOperation("查看指定海关代码表数据")
    public JsonResult detail(@PathVariable String code) {
        return JsonResult.data(Params.param(M_DETAIL, service.getOne(Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, code))));
    }

    /**
     * @return int
     * @explain 保存海关代码表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存海关代码表", notes = "保存海关代码表[pCustomsCode],作者：ZhangPeng")
    public JsonResult save(@RequestBody PCustomsCode pCustomsCode) {
        return JsonResult.data(service.save(pCustomsCode));
    }

    /**
     * @return int
     * @explain 单个删除海关代码表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @DeleteMapping(value = CODE)
    @ApiOperation(value = "单个删除海关代码表", notes = "单个删除海关代码表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String code) {
        return JsonResult.data(service.remove(Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, code)));
    }

    /**
     * @return int
     * @explain 批量删除海关代码表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @DeleteMapping
    @ApiOperation(value = "批量删除海关代码表", notes = "批量删除海关代码表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("codes") String... codes) {
        // 循环遍历删除
        Arrays.asList(codes).stream().forEach(code -> {
            service.remove(Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, code));
        });

        return JsonResult.success();
    }

    /**
     * @return pCustomsCode
     * @explain 更新海关代码表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @PutMapping(value = CODE)
    @ApiOperation(value = "更新海关代码表", notes = "更新海关代码表[pCustomsCode],作者：ZhangPeng")
    public JsonResult update(@PathVariable String code, @RequestBody PCustomsCode pCustomsCode) {
        return JsonResult.data(service.update(pCustomsCode, Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, code)));
    }

}