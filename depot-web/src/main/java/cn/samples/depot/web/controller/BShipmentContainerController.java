/**
 * @filename:BShipmentContainerController 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.entity.BShipmentContainer;
import cn.samples.depot.web.service.BShipmentContainerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.controller.BShipmentContainerController.API;

/**
 * @Description: 集装箱信息表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Api(tags = "发运计划-发运计划申请-集装箱信息", value = "集装箱信息表")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class BShipmentContainerController {

    static final String API = "/BShipmentContainer";

    @Autowired
    private BShipmentContainerService service;

    /**
     * @return PageInfo<BShipmentContainer>
     * @explain 分页条件查询集装箱信息表
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @GetMapping
    // @Cacheable(value = "bShipmentContainer", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<BShipmentContainer>],作者：ZhangPeng")
    public JsonResult index(@Valid BShipmentContainer bShipmentContainer,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BShipmentContainer> wrapper = new QueryWrapper<>();
        if (null != bShipmentContainer.getContainerNo()) {
            wrapper.lambda().eq(BShipmentContainer::getContainerNo, bShipmentContainer.getContainerNo());
        }

        // 查询第1页，每页返回10条
        Page<BShipmentContainer> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bShipmentContainer).orElseGet(BShipmentContainer::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * Description: 查看指定集装箱信息表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月24日
     **/
    @AddLog
    // @Cacheable(value = "bShipmentContainer", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看指定集装箱信息表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存集装箱信息表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "bShipmentContainer", key = "#bShipmentContainer.id")
    @ApiOperation(value = "保存集装箱信息表", notes = "保存集装箱信息表[bShipmentContainer],作者：ZhangPeng")
    public JsonResult save(@RequestBody BShipmentContainer bShipmentContainer) {
        return JsonResult.data(service.save(bShipmentContainer));
    }

    /**
     * @return int
     * @explain 单个删除集装箱信息表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "bShipmentContainer", key = "#id")
    @ApiOperation(value = "单个删除集装箱信息表", notes = "单个删除集装箱信息表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除集装箱信息表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "bShipmentContainer", allEntries = true)
    @ApiOperation(value = "批量删除集装箱信息表", notes = "批量删除集装箱信息表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return bShipmentContainer
     * @explain 更新集装箱信息表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新集装箱信息表", notes = "更新集装箱信息表[bShipmentContainer],作者：ZhangPeng")
    // @CachePut(value = "bShipmentContainer", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody BShipmentContainer bShipmentContainer) {
        return JsonResult.data(service.update(bShipmentContainer, Wrappers.<BShipmentContainer>lambdaQuery().eq(BShipmentContainer::getId, id)));
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增集装箱信息表", notes = "新增集装箱信息数据[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BShipmentContainer()));
    }
}