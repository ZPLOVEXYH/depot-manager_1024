/**
 * @filename:BContainerInfo 2019年09月20日
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
 * @Description: 集装箱状态信息表(用于查询集装当前状态信息)
 * @Author: ZhangPeng
 * @CreateDate: 2019年09月20日
 * @Version: V1.0
 */
@TableName("b_container_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BContainerInfo extends Model<BContainerInfo> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 箱号
     */
    @ApiModelProperty(name = "contaNo", value = "箱号")
    @TableField(value = "conta_no")
    private String contaNo;


    /**
     * 箱型
     */
    @ApiModelProperty(name = "contaType", value = "箱型")
    @TableField(value = "conta_type")
    private String contaType;


    /**
     * 铅封号
     */
    @ApiModelProperty(name = "sealNo", value = "铅封号")
    @TableField(value = "seal_no")
    private String sealNo;


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
    @TableField(value = "station_name")
    private String stationName;


    /**
     * 进场时间
     */
    @ApiModelProperty(name = "inTime", value = "进场时间")
    @TableField(value = "in_time")
    private Long inTime;


    /**
     * 出场时间
     */
    @ApiModelProperty(name = "outTime", value = "出场时间")
    @TableField(value = "out_time")
    private Long outTime;


    /**
     * 箱状态
     */
    @ApiModelProperty(name = "status", value = "箱状态")
    @TableField(value = "status")
    private String status;


    /**
     * 运抵放行
     */
    @ApiModelProperty(name = "arriveRel", value = "运抵放行")
    @TableField(value = "arrive_rel")
    private String arriveRel;


    /**
     * 理货放行
     */
    @ApiModelProperty(name = "tallyRel", value = "理货放行")
    @TableField(value = "tally_rel")
    private String tallyRel;


    /**
     * 装车记录放行
     */
    @ApiModelProperty(name = "loadRel", value = "装车记录放行")
    @TableField(value = "load_rel")
    private String loadRel;


    /**
     * 发货企业
     */
    @ApiModelProperty(name = "shipCompany", value = "发货企业")
    @TableField(value = "ship_company")
    private String shipCompany;


    /**
     * 提货企业
     */
    @ApiModelProperty(name = "deliveryCompany", value = "提货企业")
    @TableField(value = "delivery_company")
    private String deliveryCompany;


    /**
     * 进出口标记
     */
    @ApiModelProperty(name = "iEFlag", value = "进出口标记")
    @TableField(value = "i_e_flag")
    private String iEFlag;


    /**
     * 更新时间
     */
    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @TableField(value = "update_time")
    private Long updateTime;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();

    @ApiModelProperty(name = "shipmentPlanNo", value = "发运计划编号")
    @TableField(exist = false)
    private String shipmentPlanNo;

    @ApiModelProperty(name = "billNo", value = "提运单号")
    @TableField(exist = false)
    private String billNo;

    @ApiModelProperty(name = "areaName", value = "堆区")
    @TableField(exist = false)
    private String areaName;

    @ApiModelProperty(name = "areaPositionName", value = "堆位")
    @TableField(exist = false)
    private String areaPositionName;

}
