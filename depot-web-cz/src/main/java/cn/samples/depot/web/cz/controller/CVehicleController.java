package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.CVehicleQuery;
import cn.samples.depot.web.cz.service.CVehicleService;
import cn.samples.depot.web.entity.CVehicle;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.CVehicleController.API;

/**
 * @Author majunzi
 * @Date 2019/10/15
 * @Description 车辆备案
 **/
@Api(tags = "基础信息-车辆备案")
@RestController
@RequestMapping(value = API)
@Slf4j
public class CVehicleController {

    static final String API = URL_PRE_STATION + "/CVehicle";

    @Autowired
    private CVehicleService service;


    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 分页查询
     **/
    @AddLog
    @GetMapping
//    @Cacheable(value = "cVehicle", sync = true)
    @ApiOperation(value = "分页查询")
    @JsonView(CVehicle.View.Table.class)
    public JsonResult index(@Valid CVehicleQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<CVehicle> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(StringUtils.isNotEmpty(query.getVehicleNumber()), CVehicle::getVehicleNumber, query.getVehicleNumber())
                .eq(StringUtils.isNotEmpty(query.getEnterpriseCode()), CVehicle::getEnterpriseCode, query.getEnterpriseCode())
                .like(StringUtils.isNotEmpty(query.getEnterpriceName()), CVehicle::getEnterpriceName, query.getEnterpriceName())
                .orderByDesc(CVehicle::getCreateTime);

        // 查询第1页，每页返回10条
        Page<CVehicle> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(query).orElseGet(CVehicleQuery::new)));
    }

    @GetMapping(SELECT)
    @ApiOperation(value = "下拉查询", notes = "返回[List<CVehicle>],作者：chenjie")
//    @Cacheable(value = "cVehicle", sync = true)
    public JsonResult select() {
        QueryWrapper<CVehicle> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CVehicle::getEnable, Status.ENABLED.getValue());
        return JsonResult.data(service.list(wrapper));
    }


    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 查看
     **/
    @AddLog
//    @Cacheable(value = "cVehicle", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看")
    @JsonView(CVehicle.View.Table.class)
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 新增
     **/
    @GetMapping(value = NEW)
    @ApiOperation(value = "新增")
    @JsonView(CVehicle.View.Table.class)
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, CVehicle.builder().build())
        );
    }

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 保存
     **/
    @AddLog
//    @CachePut(value = "cVehicle", key = "#cVehicle.id")
    @PostMapping
    @ApiOperation(value = "保存")
    @JsonView(CVehicle.View.Table.class)
    public JsonResult save(@RequestBody CVehicle cVehicle) throws Throwable {
        service.saveVehical(cVehicle);
        return JsonResult.success();
    }

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 删除
     **/
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "删除")
//    @CacheEvict(value = "cVehicle", key = "#id")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }


    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 修改
     **/
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新")
//    @CachePut(value = "cVehicle", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody CVehicle cVehicle) throws Throwable {
        service.updateVehical(id, cVehicle);
        return JsonResult.success();
    }

}