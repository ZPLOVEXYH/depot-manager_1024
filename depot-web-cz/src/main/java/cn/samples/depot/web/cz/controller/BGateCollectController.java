/**
 * @filename:BGateCollectController 2019年08月01日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.ChannelTypeFlag;
import cn.samples.depot.common.model.IEFlag;
import cn.samples.depot.common.model.RelTypeFlag;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.cz.service.BGateCollectService;
import cn.samples.depot.web.cz.service.CStationsService;
import cn.samples.depot.web.entity.BGateCollect;
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
import static cn.samples.depot.web.cz.controller.BGateCollectController.API;

/**
 * @Description: 过卡信息采集表接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
@Api(tags = "海关业务-过卡信息查询", value = "过卡信息采集表")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BGateCollectController {

    static final String API = "/BGateCollect";

    @Autowired
    private BGateCollectService service;

    @Autowired
    private CStationsService stationsService;

    /**
     * @return PageInfo<BGateCollect>
     * @explain 分页条件查询过卡信息采集表
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<BGateCollect>],作者：ZhangPeng")
    public JsonResult index(@Valid BGateCollect bGateCollect,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BGateCollect> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq((bGateCollect.getStationCode() != null && Strings.EMPTY != bGateCollect.getStationCode()), BGateCollect::getStationCode, bGateCollect.getStationCode()) // 场站名称
                .eq((bGateCollect.getChannelType() != null && Strings.EMPTY != bGateCollect.getChannelType()), BGateCollect::getChannelType, bGateCollect.getChannelType())// 通道进出类型（00：进场站通道、10：出场站通道）
                .eq((bGateCollect.getRelType() != null && Strings.EMPTY != bGateCollect.getRelType()), BGateCollect::getRelType, bGateCollect.getRelType())// 抬杆类型（00：自动抬杆、10：人工抬杆，99：不抬杆）
                .like((bGateCollect.getVehicleNumber() != null && Strings.EMPTY != bGateCollect.getVehicleNumber()), BGateCollect::getVehicleNumber, bGateCollect.getVehicleNumber())// 采集车牌号
                .like((bGateCollect.getAfterContainerNo() != null && Strings.EMPTY != bGateCollect.getAfterContainerNo()), BGateCollect::getAfterContainerNo, bGateCollect.getContainerNo())// 集装箱号
                .like((bGateCollect.getFrontContainerNo() != null && Strings.EMPTY != bGateCollect.getFrontContainerNo()), BGateCollect::getFrontContainerNo, bGateCollect.getContainerNo())// 集装箱号
                .ge((bGateCollect.getStartEntryTime() != null && bGateCollect.getEndEntryTime() == null), BGateCollect::getEntryTime, bGateCollect.getStartEntryTime())
                .le((bGateCollect.getStartEntryTime() == null && bGateCollect.getEndEntryTime() != null), BGateCollect::getEntryTime, bGateCollect.getEndEntryTime())
                .between((bGateCollect.getStartEntryTime() != null && bGateCollect.getEndEntryTime() != null), BGateCollect::getEntryTime, bGateCollect.getStartEntryTime(), bGateCollect.getEndEntryTime()); // 开始货到时间，结束货到时间

        // 查询第1页，每页返回10条
        Page<BGateCollect> page = new Page<>(pageNum, pageSize);
        IPage<BGateCollect> bGateCollectIPage = service.page(page, wrapper);
        if (null != bGateCollectIPage) {
            List<BGateCollect> gateCollects = bGateCollectIPage.getRecords();
            gateCollects.forEach(x -> {
                CStations cStations = stationsService.getOne(new QueryWrapper<CStations>().lambda().eq(CStations::getCode, x.getStationCode()));
                if (null != cStations) {
                    x.setStationName(cStations.getName());
                }

                // 获取得到通道编号
                String channelCode = x.getChannelCode();
                if (StringUtil.isNotEmpty(channelCode)) {
                    // TODO 根据通道编码获取得到通道中文名称
                }
            });
        }


        return JsonResult.data(Params.param(M_PAGE, bGateCollectIPage)
                .set("query", Optional.ofNullable(bGateCollect).orElseGet(BGateCollect::new))
                .set("channelType", ChannelTypeFlag.map.values())
                .set("relTypeFlag", RelTypeFlag.map.values())
                .set("ieFlag", IEFlag.values()));
    }

    /**
     * Description: 查看指定过卡信息采集表数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月01日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看指定过卡信息采集表数据")
    public JsonResult detail(@PathVariable String id) {
        BGateCollect bGateCollect = service.getById(id);
        if (null != bGateCollect) {
            CStations cStations = stationsService.getOne(new QueryWrapper<CStations>().lambda().eq(CStations::getCode, bGateCollect.getStationCode()));
            if (null != cStations) {
                bGateCollect.setStationName(cStations.getName());
            }
        }

//        // 获取得到通道编号
//        String channelCode = bGateCollect.getChannelCode();
//        if (StringUtil.isNotEmpty(channelCode)) {
//            // TODO 根据通道编码获取得到通道中文名称
//        }
        return JsonResult.data(Params.param(M_DETAIL, bGateCollect)
                .set("channelType", ChannelTypeFlag.map.values())
                .set("relTypeFlag", RelTypeFlag.map.values())
                .set("ieFlag", IEFlag.values()));
    }

    /**
     * Description: 根据卡口序列号查询过卡信息数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年10月22日
     **/
    @AddLog
    @GetMapping(value = "/queryDetailByGateNo/{gateNo}")
    @ApiOperation("根据卡口序列号查询过卡信息")
    public JsonResult queryDetailByGateNo(@PathVariable String gateNo) {
        BGateCollect bGateCollect = service.getOne(Wrappers.<BGateCollect>lambdaQuery().eq(BGateCollect::getGateNo, gateNo));
        if (null != bGateCollect) {
            CStations cStations = stationsService.getOne(new QueryWrapper<CStations>().lambda().eq(CStations::getCode, bGateCollect.getStationCode()));
            if (null != cStations) {
                bGateCollect.setStationName(cStations.getName());
            }
        }

        return JsonResult.data(Params.param(M_DETAIL, bGateCollect)
                .set("channelType", ChannelTypeFlag.map.values())
                .set("relTypeFlag", RelTypeFlag.map.values())
                .set("ieFlag", IEFlag.values()));
    }

    /**
     * Description: 根据车牌号查询过卡详细信息
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年09月27日
     **/
    @AddLog
    @GetMapping("/vn/{vehicleNumber}")
    @ApiOperation("根据车牌号查询过卡详细信息")
    public JsonResult detailByVehicleNumber(@PathVariable String vehicleNumber) {
        List<BGateCollect> bGateCollectList = service.list(Wrappers.<BGateCollect>lambdaQuery().eq(BGateCollect::getVehicleNumber, vehicleNumber).orderByDesc(BGateCollect::getCreateTime));
        BGateCollect bGateCollect = CollectionUtil.isNotEmpty(bGateCollectList) ? bGateCollectList.get(0) : null;
        if (null != bGateCollect) {
            CStations cStations = stationsService.getOne(new QueryWrapper<CStations>().lambda().eq(CStations::getCode, bGateCollect.getStationCode()));
            if (null != cStations) {
                bGateCollect.setStationName(cStations.getName());
            }
        }

//        // 获取得到通道编号
//        String channelCode = bGateCollect.getChannelCode();
//        if (StringUtil.isNotEmpty(channelCode)) {
//            // TODO 根据通道编码获取得到通道中文名称
//        }
        return JsonResult.data(Params.param(M_DETAIL, bGateCollect)
                .set("channelType", ChannelTypeFlag.map.values())
                .set("relTypeFlag", RelTypeFlag.map.values())
                .set("ieFlag", IEFlag.values()));
    }

    /**
     * @return int
     * @explain 保存过卡信息采集表对象
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @PostMapping
    // @CachePut(value = "bGateCollect", key = "#bGateCollect.id")
    @ApiOperation(value = "保存过卡信息采集表", notes = "保存过卡信息采集表[bGateCollect],作者：ZhangPeng")
    public JsonResult save(@RequestBody BGateCollect bGateCollect) {
        return JsonResult.data(service.save(bGateCollect));
    }

    /**
     * @return int
     * @explain 单个删除过卡信息采集表对象
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @DeleteMapping(value = ID)
    // @CacheEvict(value = "bGateCollect", key = "#id")
    @ApiOperation(value = "单个删除过卡信息采集表", notes = "单个删除过卡信息采集表,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除过卡信息采集表对象
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @DeleteMapping
    // @CacheEvict(value = "bGateCollect", allEntries = true)
    @ApiOperation(value = "批量删除过卡信息采集表", notes = "批量删除过卡信息采集表,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return bGateCollect
     * @explain 更新过卡信息采集表对象
     * @author ZhangPeng
     * @time 2019年08月01日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新过卡信息采集表", notes = "更新过卡信息采集表[bGateCollect],作者：ZhangPeng")
    // @CachePut(value = "bGateCollect", key = "#id")
    public JsonResult update(@PathVariable String id, @RequestBody BGateCollect bGateCollect) {
        return JsonResult.data(service.update(bGateCollect, Wrappers.<BGateCollect>lambdaQuery().eq(BGateCollect::getId, id)));
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增过卡信息采集表", notes = "新增过卡信息采集表[bShipmentPlan],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BGateCollect()));
    }

}