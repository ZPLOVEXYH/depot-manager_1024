/**
 * @filename:BGateCollect 2019年08月01日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.entity;

import cn.samples.depot.common.model.CRUDView;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 过卡信息采集表
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
@TableName("b_gate_collect")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BGateCollect extends Model<BGateCollect> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     * 审核日志id
     */
    @ApiModelProperty(name = "id", value = "审核日志id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 卡口序列号
     */
    @ApiModelProperty(name = "gateNo", value = "卡口序列号")
    @TableField(value = "gate_no")
    private String gateNo;


    /**
     * 场站编码
     */
    @ApiModelProperty(name = "stationCode", value = "场站编码")
    @TableField(value = "station_code")
    private String stationCode;

    /**
     * 场站名称
     */
    @ApiModelProperty(name = "stationName", value = "场站名称")
    @TableField(exist = false)
    private String stationName;


    /**
     * 通道编码
     */
    @ApiModelProperty(name = "channelCode", value = "通道编码")
    @TableField(value = "channel_code")
    private String channelCode;


    /**
     * 通道进出类型（00：进场站通道、10：出场站通道）
     */
    @ApiModelProperty(name = "channelType", value = "通道进出类型（00：进场站通道、10：出场站通道）")
    @TableField(value = "channel_type")
    private String channelType;


    /**
     * 采集车牌号
     */
    @ApiModelProperty(name = "vehicleNumber", value = "采集车牌号")
    @TableField(value = "vehicle_number")
    private String vehicleNumber;


    /**
     * 前集装箱号
     */
    @ApiModelProperty(name = "frontContainerNo", value = "前集装箱号")
    @TableField(value = "front_container_no")
    private String frontContainerNo;


    /**
     * 后集装箱号
     */
    @ApiModelProperty(name = "afterContainerNo", value = "后集装箱号")
    @TableField(value = "after_container_no")
    private String afterContainerNo;

    /**
     * 装箱号
     */
    @ApiModelProperty(name = "containerNo", value = "装箱号")
    @TableField(exist = false)
    private String containerNo;


    /**
     * 地磅称重
     */
    @ApiModelProperty(name = "groundWeight", value = "地磅称重")
    @TableField(value = "ground_weight")
    private Double groundWeight;


    /**
     * 卡口放行指令
     */
    @ApiModelProperty(name = "customsRelType", value = "卡口放行指令")
    @TableField(value = "customs_rel_type")
    private String customsRelType;


    /**
     * 抬杆类型（00：自动抬杆、10：人工抬杆，99：不抬杆）
     */
    @ApiModelProperty(name = "relType", value = "抬杆类型（00：自动抬杆、10：人工抬杆，99：不抬杆）")
    @TableField(value = "rel_type")
    private String relType;


    /**
     * 过卡采集时间
     */
    @ApiModelProperty(name = "entryTime", value = "过卡采集时间")
    @TableField(value = "entry_time")
    private Long entryTime;

    /**
     * 过卡采集开始时间
     */
    @ApiModelProperty(name = "startEntryTime", value = "过卡采集开始时间")
    @TableField(exist = false)
    private Long startEntryTime;

    /**
     * 过卡采集结束时间
     */
    @ApiModelProperty(name = "endEntryTime", value = "过卡采集结束时间")
    @TableField(exist = false)
    private Long endEntryTime;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


}
