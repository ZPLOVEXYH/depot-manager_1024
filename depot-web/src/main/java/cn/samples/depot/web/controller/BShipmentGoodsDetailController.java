/**
 * @filename:BShipmentGoodsDetailController 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.entity.BShipmentGoodsDetail;
import cn.samples.depot.web.service.BShipmentGoodsDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.controller.BShipmentGoodsDetailController.API;

/**
 * @Description: 商品明细表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Api(tags = "发运计划-发运计划申请-商品明细表", value = "商品明细表")
@RestController
@RequestMapping(value = URL_PRE_ENTERPRICE + API)
@Slf4j
public class BShipmentGoodsDetailController {

    static final String API = "/BShipmentGoodsDetail";

    @Autowired
    private BShipmentGoodsDetailService service;

    /**
     * @return PageInfo<BShipmentGoodsDetail>
     * @explain 分页条件查询商品明细表
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @GetMapping
    // @Cacheable(value = "bShipmentGoodsDetail", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<BShipmentGoodsDetail>],作者：ZhangPeng")
    public JsonResult index(@Valid BShipmentGoodsDetail bShipmentGoodsDetail,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BShipmentGoodsDetail> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .like((bShipmentGoodsDetail.getContainerNo() != null && Strings.EMPTY != bShipmentGoodsDetail.getContainerNo()), BShipmentGoodsDetail::getContainerNo, bShipmentGoodsDetail.getContainerNo()) // code全匹配查询
                .like((bShipmentGoodsDetail.getGoodsName() != null && Strings.EMPTY != bShipmentGoodsDetail.getGoodsName()), BShipmentGoodsDetail::getGoodsName, bShipmentGoodsDetail.getGoodsName());

        // 查询第1页，每页返回10条
        Page<BShipmentGoodsDetail> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bShipmentGoodsDetail).orElseGet(BShipmentGoodsDetail::new)));
    }

    /**
     * Description: 查看指定商品明细表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年7月24日
     **/
    @AddLog
    // @Cacheable(value = "bShipmentGoodsDetail", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看指定商品明细表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存商品明细表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "bShipmentGoodsDetail", key = "#bShipmentGoodsDetail.id")
    @ApiOperation(value = "保存商品明细表", notes = "保存商品明细表[bShipmentGoodsDetail],作者：ZhangPeng")
    public JsonResult save(@RequestBody BShipmentGoodsDetail bShipmentGoodsDetail) {
        return JsonResult.data(service.save(bShipmentGoodsDetail));
    }

    /**
     * @return int
     * @explain 单个删除商品明细表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "bShipmentGoodsDetail", key = "#id")
    @ApiOperation(value = "单个删除商品明细表", notes = "单个删除商品明细表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除商品明细表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "bShipmentGoodsDetail", allEntries = true)
    @ApiOperation(value = "批量删除商品明细表", notes = "批量删除商品明细表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return bShipmentGoodsDetail
     * @explain 更新商品明细表对象
     * @author ZhangPeng
     * @time 2019年7月24日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新商品明细表", notes = "更新商品明细表[bShipmentGoodsDetail],作者：ZhangPeng")
    // @CachePut(value = "bShipmentGoodsDetail", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody BShipmentGoodsDetail bShipmentGoodsDetail) {
        return JsonResult.data(service.update(bShipmentGoodsDetail, Wrappers.<BShipmentGoodsDetail>lambdaQuery().eq(BShipmentGoodsDetail::getId, id)));
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增商品明细表", notes = "新增商品明细数据[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BShipmentGoodsDetail()));
    }

}