/**
 * @filename:BShipmentPlanService 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.service;

import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.bean.BShipmentPlanDTO;
import cn.samples.depot.web.entity.BShipmentPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 发运计划表——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
public interface BShipmentPlanService extends IService<BShipmentPlan> {

    /**
     * 保存发运计划信息
     *
     * @param dto
     * @return
     */
    JsonResult saveBShipmentPlan(BShipmentPlanDTO dto);

    /**
     * 根据id删除发运计划
     *
     * @param id
     * @return
     */
    JsonResult deleteBShipmentPlan(String id);

    /**
     * 更新发运计划表
     *
     * @param id
     * @param bShipmentPlanDTO
     * @return
     */
    JsonResult updateShipmentPlan(String id, BShipmentPlanDTO bShipmentPlanDTO);

    /**
     * 提交发运计划给场站
     *
     * @return
     */
    JsonResult senderForApply(String... ids);

    /**
     * 导入excel
     *
     * @param file
     * @return
     */
    void uploadExcel(String stationsCode, MultipartFile file, HttpServletResponse response);

    /**
     * @Author majunzi
     * @Date 2019/9/29
     * @Description 根据id获取详情
     **/
    BShipmentPlanDTO detail(String id);
}