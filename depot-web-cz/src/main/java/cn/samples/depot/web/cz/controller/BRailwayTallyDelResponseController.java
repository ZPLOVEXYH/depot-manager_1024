/**
 * @filename:BRailwayTallyDelResponseController 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.ChkFlag;
import cn.samples.depot.common.model.MessageTypeFlag;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.cz.service.BRailwayTallyDelResponseService;
import cn.samples.depot.web.cz.service.CStationsService;
import cn.samples.depot.web.cz.service.PCustomsCodeService;
import cn.samples.depot.web.entity.BRailwayTallyDelResponse;
import cn.samples.depot.web.entity.PCustomsCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
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
import static cn.samples.depot.web.cz.controller.BRailwayTallyDelResponseController.API;

/**
 * @Description: 铁路进口理货作废报文回执接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Api(tags = "海关业务-理货作废-回执表头", value = "铁路进口理货作废报文回执")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BRailwayTallyDelResponseController {

    static final String API = "/BRailwayTallyDelResponse";

    @Autowired
    private BRailwayTallyDelResponseService service;

    @Autowired
    private CStationsService cStationsService;

    @Autowired
    private PCustomsCodeService pCustomsCodeService;

    /**
     * @return PageInfo<BRailwayTallyDelResponse>
     * @explain 分页条件查询铁路进口理货作废报文回执
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询返回对象[PageInfo<BRailwayTallyDelResponse>],作者：ZhangPeng")
    public JsonResult index(@Valid BRailwayTallyDelResponse bRailwayTallyDelResponse,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BRailwayTallyDelResponse> wrapper = new QueryWrapper<>();
        // wrapper.lambda()
        // .eq((bShipmentPlanQuery.getEnterprisesId() != null && Strings.EMPTY != bShipmentPlanQuery.getEnterprisesId()), BShipmentPlan::getEnterprisesId, bShipmentPlanQuery.getEnterprisesId());// 发运计划编号


        // 查询第1页，每页返回10条
        Page<BRailwayTallyDelResponse> page = new Page<>(pageNum, pageSize);

        return JsonResult.data(Params.param(M_PAGE, service.page(page, wrapper))
                .set("query", Optional.ofNullable(bRailwayTallyDelResponse).orElseGet(BRailwayTallyDelResponse::new))
                .set("status", Lists.newArrayList(Status.ENABLED, Status.DISABLED)));
    }

    /**
     * Description: 查看指定铁路进口理货作废报文回执数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月12日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation("查看指定铁路进口理货作废报文回执数据")
    public JsonResult detail(@PathVariable String id) {
        return JsonResult.data(Params.param(M_DETAIL, service.getById(id)));
    }

    /**
     * @return int
     * @explain 保存铁路进口理货作废报文回执对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @PostMapping
    @ApiOperation(value = "保存铁路进口理货作废报文回执", notes = "保存铁路进口理货作废报文回执[bRailwayTallyDelResponse],作者：ZhangPeng")
    public JsonResult save(@RequestBody BRailwayTallyDelResponse bRailwayTallyDelResponse) {
        return JsonResult.data(service.save(bRailwayTallyDelResponse));
    }

    /**
     * @return int
     * @explain 单个删除铁路进口理货作废报文回执对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @DeleteMapping(value = ID)
    @ApiOperation(value = "单个删除铁路进口理货作废报文回执", notes = "单个删除铁路进口理货作废报文回执,作者：ZhangPeng")
    public JsonResult deleteSingle(@PathVariable String id) {
        return JsonResult.data(service.removeById(id));
    }

    /**
     * @return int
     * @explain 批量删除铁路进口理货作废报文回执对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @DeleteMapping
    @ApiOperation(value = "批量删除铁路进口理货作废报文回执", notes = "批量删除铁路进口理货作废报文回执,作者：ZhangPeng")
    public JsonResult deleteBatch(@RequestParam("ids") String... ids) {
        return JsonResult.data(service.removeByIds(Arrays.asList(ids)));
    }

    /**
     * @return bRailwayTallyDelResponse
     * @explain 更新铁路进口理货作废报文回执对象
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @PutMapping(value = ID)
    @ApiOperation(value = "更新铁路进口理货作废报文回执", notes = "更新铁路进口理货作废报文回执[bRailwayTallyDelResponse],作者：ZhangPeng")
    public JsonResult update(@PathVariable String id, @RequestBody BRailwayTallyDelResponse bRailwayTallyDelResponse) {
        return JsonResult.data(service.update(bRailwayTallyDelResponse, Wrappers.<BRailwayTallyDelResponse>lambdaQuery().eq(BRailwayTallyDelResponse::getId, id)));
    }

    @GetMapping(value = NEW)
    @ApiOperation(value = "新增铁路进口理货作废报文回执", notes = "新增铁路进口理货作废报文回执[bRailwayTallyDelResponse],作者：ZhangPeng")
    public JsonResult create() {
        return JsonResult.data(Params.param(M_DETAIL, new BRailwayTallyDelResponse()));
    }

    @PostMapping(value = DECL_MESSAGE_ID + MESSAGE_TYPE)
    @ApiOperation(value = "根据申请报文编号分页查询作废报文回执", notes = "根据申请报文编号分页查询作废报文回执[bRailwayTallyDelResponse],作者：ZhangPeng")
    public JsonResult queryByDeclMessageId(@PathVariable String declMessageId,
                                           @PathVariable String messageType,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<BRailwayTallyDelResponse> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq((declMessageId != null && Strings.EMPTY != declMessageId),
                        // 申请报文编号
                        BRailwayTallyDelResponse::getDeclMessageId, declMessageId)
                .eq((messageType != null && Strings.EMPTY != messageType),
                        // 报文类型
                        BRailwayTallyDelResponse::getMessageType, messageType);

        // 查询第1页，每页返回10条
        Page<BRailwayTallyDelResponse> page = new Page<>(pageNum, pageSize);
        IPage<BRailwayTallyDelResponse> iPage = service.page(page, wrapper);
        if (null != iPage) {
            List<BRailwayTallyDelResponse> delResponseList = iPage.getRecords();
            if (CollectionUtil.isNotEmpty(delResponseList)) {
                delResponseList.forEach(ways -> {
                    // 报文发送方的id
                    String sendId = ways.getSendId();
//                    CStations cStations = cStationsService.getOne(Wrappers.<CStations>lambdaQuery().eq(CStations::getCode, sendId).eq(CStations::getEnable, Status.ENABLED.getValue()));
//                    if (null != cStations) {
//                        // 获取得到中文的海关名称
//                        String sendName = cStations.getName();
//                        ways.setSendName(sendName);
//                    }

                    PCustomsCode pCustomsCode = pCustomsCodeService.getOne(Wrappers.<PCustomsCode>lambdaQuery().eq(PCustomsCode::getCode, sendId).eq(PCustomsCode::getEnable, Status.ENABLED.getValue()));
                    if (null != pCustomsCode) {
                        // 根据发送方code获取得到海关名称
                        String sendName = pCustomsCode.getName();
                        ways.setSendName(sendName);
                    }
                });
            }
        }

        return JsonResult.data(Params.param(M_PAGE, iPage)
                // 报文类型
                .set("messageTypeFlag", MessageTypeFlag.values())
                // 回执类型
                .set("chkFlag", ChkFlag.values()));
    }

}