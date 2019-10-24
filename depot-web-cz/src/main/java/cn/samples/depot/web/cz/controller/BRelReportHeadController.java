/**
 * @filename:BRelReportHeadController 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.controller;

import cn.samples.depot.common.config.aop.AddLog;
import cn.samples.depot.common.model.*;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.Params;
import cn.samples.depot.web.bean.report.BRelReportHeadQuery;
import cn.samples.depot.web.cz.service.BRelReportHeadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static cn.samples.depot.common.utils.Controllers.*;
import static cn.samples.depot.web.cz.controller.BRelReportHeadController.API;

/**
 * @Description: 放行指令表头接口层
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Api(tags = "海关业务-海关指令-放行指令", value = "海关指令->放行指令")
@RestController
@RequestMapping(value = URL_PRE_STATION + API)
@Slf4j
public class BRelReportHeadController {

    static final String API = "/BRelReportHead";

    @Autowired
    private BRelReportHeadService service;

    /**
     * @return PageInfo<BRelReportHead>
     * @explain 分页条件查询放行指令表头
     * @author ZhangPeng
     * @time 2019年08月12日
     */
    @AddLog
    @GetMapping
    @ApiOperation(value = "海关指令->放行指令->分页列表查询", notes = "分页列表查询")
    public JsonResult index(@Valid BRelReportHeadQuery query,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return JsonResult.data(Params.param(M_PAGE, service.selectRelReportListPage(query, pageNum, pageSize))
                .set("query", Optional.ofNullable(query).orElseGet(BRelReportHeadQuery::new))
                // 放行指令，报文类型
                .set("relMessageTypeFlag", RelMessageTypeFlag.values())
                // 集装箱放行方式
                .set("relModeFlag", RelModeFlag.values())
                // 单证类型
                .set("formTypeFlag", FormTypeFlag.values())
                // 提单放行方式
                .set("relTypeFlag", RelTypeFlag.values())
                // 航线标记
                .set("lineFlag", LineFlag.values())
                // 进出口标记
                .set("ieFlag", IEFlag.values()));
    }

    /**
     * Description: 查看指定放行指令表头数据
     *
     * @return: JsonResult
     * @author ZhangPeng
     * @time 2019年08月12日
     **/
    @AddLog
    @GetMapping(value = ID)
    @ApiOperation(value = "海关指令->放行指令->查看放行指令明细", notes = "根据放行指令id查看放行指令明细信息")
    public JsonResult detail(@PathVariable String id) {
        return service.queryRelReportDetail(id);
    }
}