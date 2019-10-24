/**
 * @filename:CEnterprisesController 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.enterprises.CEnterprisesQuery;
import cn.samples.depot.web.entity.CEnterprises;
import cn.samples.depot.web.service.CEnterprisesService;
import cn.samples.depot.web.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.controller.CEnterprisesController.API;

/**
 * @Description: 企业信息表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Api(tags = "基础信息-企业管理", value = "企业信息表")
@RestController
@RequestMapping(value = API)
@SuppressWarnings("rawtypes")
public class CEnterprisesController {

    static final String API = URL_PRE_COMMON + "/CEnterprises";

    @Autowired
    private CEnterprisesService service;
    @Autowired
    private UserService userService;

    /**
     * @return PageInfo<CEnterprises>
     * @explain 分页条件查询企业信息表
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<CEnterprises>],作者：ZhangPeng")
    public JsonResult index(@Valid CEnterprisesQuery cEnterprises,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CEnterprises> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(cEnterprises.getEnable() != null, CEnterprises::getEnable, cEnterprises.getEnable())
                .like(cEnterprises.getName() != null, CEnterprises::getName, cEnterprises.getName())
                .eq(cEnterprises.getEnterpriseTypeCode() != null, CEnterprises::getEnterpriseTypeCode, cEnterprises.getEnterpriseTypeCode())
                .eq(cEnterprises.getOrgCode() != null, CEnterprises::getOrgCode, cEnterprises.getOrgCode())
                .eq(cEnterprises.getCreditCode() != null, CEnterprises::getCreditCode, cEnterprises.getCreditCode())
                .like(cEnterprises.getContact() != null, CEnterprises::getContact, cEnterprises.getContact())
                .eq(cEnterprises.getContactPhone() != null, CEnterprises::getContactPhone, cEnterprises.getContactPhone())
                .like(cEnterprises.getAddress() != null, CEnterprises::getAddress, cEnterprises.getAddress())
                .orderByDesc(CEnterprises::getCreateTime);

        // 查询第1页，每页返回10条
        Page<CEnterprises> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(cEnterprises).orElseGet(CEnterprisesQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * 企业下拉查询
     *
     * @return
     */
    @GetMapping(SELECT)
    @ApiOperation(value = "企业下拉查询", notes = "返回企业下拉列表,作者：ChenJie")
    @JsonView(CEnterprises.View.SELECT.class)
    public JsonResult select() {
        QueryWrapper<CEnterprises> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CEnterprises::getEnable, Status.ENABLED.getValue());
        List<CEnterprises> list = service.list(wrapper);
        return JsonResult.data(list);
    }

    @GetMapping("/me")
    @ApiOperation(value = "查询当前登录企业信息", notes = "查询当前登录企业信息,作者：ChenJie")
    public JsonResult me() {
        String code = userService.userContext().getUser().getEnterpriseCode();
        CEnterprises cEnterprises = service.getOne(Wrappers.<CEnterprises>lambdaQuery().eq(CEnterprises::getCode, code));
        return JsonResult.data(Params.param(M_DETAIL, cEnterprises));
    }

    /**
     * Description: 查看指定企业信息表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月18日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看指定企业信息表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存企业信息表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存企业信息表", notes = "保存企业信息表[cEnterprises],作者：ZhangPeng")
    public JsonResult save(@RequestBody CEnterprises cEnterprises) throws Throwable {
        if (CollectionUtils.isNotEmpty(service.list(new LambdaQueryWrapper<CEnterprises>().eq(CEnterprises::getCode, cEnterprises.getCode()))))
            throw new BizException(String.format("企业代码[%s]已存在", cEnterprises.getCode()));
        return JsonResult.data(service.save(cEnterprises));
    }

    /**
     * @return int
     * @explain 单个删除企业信息表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "单个删除企业信息表", notes = "单个删除企业信息表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return service.deleteEnterprise(id);
    }

    /**
     * @return int
     * @explain 批量删除企业信息表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @DeleteMapping
    @ApiOperation(value = "批量删除企业信息表", notes = "批量删除企业信息表,作者：ZhangPeng")
//    @CacheEvict(value = "cEnterprises", allEntries = true)
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return cEnterprises
     * @explain 更新企业信息表对象
     * @author ZhangPeng
     * @time 2019年7月19日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新企业信息表", notes = "更新企业信息表[cEnterprises],作者：ChenJie")
    public JsonResult update(@PathVariable String id, @RequestBody CEnterprises cEnterprises) {
        return service.updateEnterprise(cEnterprises);
    }

}