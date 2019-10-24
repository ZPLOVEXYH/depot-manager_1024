/**
 * @filename:BGoodsArriveConfirmController 2019年08月01日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.ConfirmStatus;
import cn.samples.depot.common.model.GoodsConfirmStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.cz.service.BGoodsArriveConfirmService;
import cn.samples.depot.web.cz.service.CStationsService;
import cn.samples.depot.web.entity.BGoodsArriveConfirm;
import cn.samples.depot.web.entity.CStations;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BGoodsArriveConfirmController.API;

/**
 * @Description: 货到确认表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
@Api(tags = "作业管理-货到确认", value = "货到确认表")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BGoodsArriveConfirmController {

    static final String API = "/BGoodsArriveConfirm";

    @Autowired
    private BGoodsArriveConfirmService service;

    @Autowired
    private CStationsService cStationsService;

    /**
     * @return PageInfo<BGoodsArriveConfirm>
     * @explain 分页条件查询货到确认表
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @GetMapping
    // @Cacheable(value = "bGoodsArriveConfirm", sync = true)
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<BGoodsArriveConfirm>],作者：ZhangPeng")
    public JsonResult index(@Valid BGoodsArriveConfirm bGoodsArriveConfirm,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BGoodsArriveConfirm> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq((bGoodsArriveConfirm.getStationCode() != null && Strings.EMPTY != bGoodsArriveConfirm.getStationCode()), BGoodsArriveConfirm::getStationCode, bGoodsArriveConfirm.getStationCode()) // 场站名称
                .like((bGoodsArriveConfirm.getVehicleNumber() != null && Strings.EMPTY != bGoodsArriveConfirm.getVehicleNumber()), BGoodsArriveConfirm::getVehicleNumber, bGoodsArriveConfirm.getVehicleNumber())// 车牌号
                .eq((bGoodsArriveConfirm.getStatus() != null && Strings.EMPTY != bGoodsArriveConfirm.getStatus()), BGoodsArriveConfirm::getStatus, bGoodsArriveConfirm.getStatus())// 货到确认状态
                .eq((bGoodsArriveConfirm.getConfirmType() != null && Strings.EMPTY != bGoodsArriveConfirm.getConfirmType()), BGoodsArriveConfirm::getConfirmType, bGoodsArriveConfirm.getConfirmType())// 确认类型
                .like((bGoodsArriveConfirm.getContainerNo() != null && Strings.EMPTY != bGoodsArriveConfirm.getContainerNo()), BGoodsArriveConfirm::getContainerNo, bGoodsArriveConfirm.getContainerNo())// 集装箱号
                .ge((bGoodsArriveConfirm.getStartEntryTime() != null && bGoodsArriveConfirm.getEndEntryTime() == null), BGoodsArriveConfirm::getEntryTime, bGoodsArriveConfirm.getStartEntryTime())
                .le((bGoodsArriveConfirm.getStartEntryTime() == null && bGoodsArriveConfirm.getEndEntryTime() != null), BGoodsArriveConfirm::getEntryTime, bGoodsArriveConfirm.getEndEntryTime())
                .between((bGoodsArriveConfirm.getStartEntryTime() != null && bGoodsArriveConfirm.getEndEntryTime() != null), BGoodsArriveConfirm::getEntryTime, bGoodsArriveConfirm.getStartEntryTime(), bGoodsArriveConfirm.getEndEntryTime()); // 开始货到时间，结束货到时间


        // 查询第1页，每页返回10条
        Page<BGoodsArriveConfirm> page = new Page<>(pageNum, pageSize);
        IPage<BGoodsArriveConfirm> ipage = service.page(page, wrapper);
        List<BGoodsArriveConfirm> arriveConfirmList = ipage.getRecords();
        if (CollectionUtil.isNotEmpty(arriveConfirmList)) {
            arriveConfirmList.forEach(arriveConfirm -> {
                // 获取得到场站编号
                String stationCode = arriveConfirm.getStationCode();
                // 根据场站编号获取得到场站名称
                if (StringUtil.isNotEmpty(stationCode)) {
                    CStations cStations = cStationsService.getOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getCode, stationCode));
                    if (null != cStations) {
                        String stationName = cStations.getName();
                        arriveConfirm.setStationName(stationName);
                    }
                }
            });
        }

        return JsonResult.data(Params.param(M_PAGE, ipage)
                .set("query", Optional.ofNullable(bGoodsArriveConfirm).orElseGet(BGoodsArriveConfirm::new))
                .set("confirmStatus", ConfirmStatus.values())// 确认类型
                .set("goodsConfirmStatus", GoodsConfirmStatus.map.values()));// 货到确认类型
    }

    /**
     * Description: 查看指定货到确认表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月01日
     **/
    @AddLog
    // @Cacheable(value = "bGoodsArriveConfirm", key = "#id")
    @GetMapping(value = ID)
    @ApiOperation("查看指定货到确认表数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存货到确认表对象
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "bGoodsArriveConfirm", key = "#bGoodsArriveConfirm.id")
    @ApiOperation(value = "保存货到确认表", notes = "保存货到确认表[bGoodsArriveConfirm],作者：ZhangPeng")
    public JsonResult save(@RequestBody BGoodsArriveConfirm bGoodsArriveConfirm) {
        return JsonResult.data(service.save(bGoodsArriveConfirm));
    }

    /**
     * @return int
     * @explain 单个删除货到确认表对象
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "bGoodsArriveConfirm", key = "#id")
    @ApiOperation(value = "单个删除货到确认表", notes = "单个删除货到确认表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除货到确认表对象
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "bGoodsArriveConfirm", allEntries = true)
    @ApiOperation(value = "批量删除货到确认表", notes = "批量删除货到确认表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return bGoodsArriveConfirm
     * @explain 更新货到确认表对象
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @PutMapping
    @ApiOperation(value = "更新货到确认表", notes = "更新货到确认表[bGoodsArriveConfirm],作者：ZhangPeng")
    public JsonResult updateBatch(@RequestBody BGoodsArriveConfirm bGoodsArriveConfirm, @RequestParam("ids") String... ids) {
        return service.updateBatch(ids, bGoodsArriveConfirm);
//        return JsonResult.data(service.update(bGoodsArriveConfirm, Wrappers.<BGoodsArriveConfirm>lambdaQuery().eq(BGoodsArriveConfirm::getId, id)));
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增货到确认表", notes = "新增货到确认表[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BGoodsArriveConfirm())
                .set("confirmStatus", ConfirmStatus.map.values())// 确认类型
                .set("goodsConfirmStatus", GoodsConfirmStatus.map.values()));// 货到确认类型);
    }

    /**
     * @return int
     * @explain 保存货到确认表对象
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @PostMapping("goods-confirm")
    // @CachePut(value = "bGoodsArriveConfirm", key = "#bGoodsArriveConfirm.id")
    @ApiOperation(value = "货到确认", notes = "货到确认[bGoodsArriveConfirm],作者：ZhangPeng")
    public JsonResult goodsConfirm(@RequestBody BGoodsArriveConfirm bGoodsArriveConfirm) {
        return JsonResult.data(service.goodsConfirm(bGoodsArriveConfirm));
    }
}