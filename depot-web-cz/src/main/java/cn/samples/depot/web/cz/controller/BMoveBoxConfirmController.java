package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.BMoveBoxConfirmQuery;
import cn.samples.depot.web.cz.service.BMoveBoxConfirmService;
import cn.samples.depot.web.entity.BMoveBoxConfirm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BMoveBoxConfirmController.API;

/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 堆场管理-移箱确认
 **/
@Api(tags = "堆场管理-移箱确认")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BMoveBoxConfirmController {

    static final String API = "/BMoveBoxConfirm";

    @Autowired
    private BMoveBoxConfirmService service;

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 分页查询
     **/
    @AddLog
    @GetMapping
    // @Cacheable(value = "bMoveBoxConfirm", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public JsonResult index(@Valid BMoveBoxConfirmQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BMoveBoxConfirm> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like((StringUtils.isNotEmpty(query.getContainerNo())), BMoveBoxConfirm::getContainerNo, query.getContainerNo())
                .ge(null != query.getStartOpTime() && query.getStartOpTime() > 0, BMoveBoxConfirm::getOpTime, query.getStartOpTime())
                .le(null != query.getEndOpTime() && query.getEndOpTime() > 0, BMoveBoxConfirm::getOpTime, query.getEndOpTime())
                .orderByDesc(BMoveBoxConfirm::getCreateTime);


        // 查询第1页，每页返回10条
        Page<BMoveBoxConfirm> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(query).orElseGet(BMoveBoxConfirmQuery::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 查看
     **/
    @AddLog
    // @Cacheable(value = "bMoveBoxConfirm", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 保存
     **/
    @AddLog
    @PostMapping
    // @CachePut(value = "bMoveBoxConfirm", key = "#bMoveBoxConfirm.id")
    @ApiOperation(value = "保存", notes = "保存")
    public JsonResult save(@RequestBody BMoveBoxConfirm bMoveBoxConfirm) throws Throwable {
        service.move(bMoveBoxConfirm);
        return JsonResult.success();
    }

    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description new
     **/
    @GetMapping(value = NEW)
    @ApiOperation(value = "新增", notes = "新增")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BMoveBoxConfirm()));
    }

}