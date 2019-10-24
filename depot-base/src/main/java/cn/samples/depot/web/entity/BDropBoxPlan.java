/**
 * @filename:BDropBoxPlan 2019年08月01日
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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 落箱计划表
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
@TableName("b_drop_box_plan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BDropBoxPlan extends Model<BDropBoxPlan> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }

        interface Arrangement extends View { //落箱安排

        }
    }

    /**
     * 审核日志id
     */
    @ApiModelProperty(name = "id", value = "审核日志id")
    @TableId(value = "id", type = IdType.UUID)
    @JsonView(value = {View.Arrangement.class})
    private String id;


    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "containerNo", value = "集装箱号")
    @TableField(value = "container_no")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private String containerNo;


    /**
     * 铅封号
     */
    @ApiModelProperty(name = "sealNo", value = "铅封号")
    @TableField(value = "seal_no")
    private String sealNo;


    /**
     * 集装箱尺寸
     */
    @ApiModelProperty(name = "contaModelName", value = "集装箱尺寸")
    @TableField(value = "conta_model_name")
    private String contaModelName;


    /**
     * 发货企业ID
     */
    @ApiModelProperty(name = "enterprisesId", value = "发货企业ID")
    @TableField(value = "enterprises_id")
    private String enterprisesId;


    /**
     * 发货企业名称
     */
    @ApiModelProperty(name = "enterprisesName", value = "发货企业名称")
    @TableField(value = "enterprises_name")
    private String enterprisesName;


    /**
     * 出运时间
     */
    @ApiModelProperty(name = "shipmentTime", value = "出运时间")
    @TableField(value = "shipment_time")
    private Long shipmentTime;


    /**
     * 堆区
     */
    @ApiModelProperty(name = "stationAreaCode", value = "堆区")
    @TableField(value = "station_area_code")
    @JsonView(value = {View.Arrangement.class})
    private String stationAreaCode;


    /**
     * 堆位
     */
    @ApiModelProperty(name = "stationAreaPositionCode", value = "堆位")
    @TableField(value = "station_area_position_code")
    @JsonView(value = {View.Arrangement.class})
    private String stationAreaPositionCode;


    /**
     * 进场时间
     */
    @ApiModelProperty(name = "entryTime", value = "进场时间")
    @TableField(value = "entry_time")
    @JsonView(value = {View.Arrangement.class})
    private Long entryTime;


    /**
     * 状态(00 待计划 01 待落箱 02 已落箱)
     */
    @ApiModelProperty(name = "status", value = "状态(00 待计划 01 待落箱 02 已落箱)")
    @TableField(value = "status")
    private String status;


    /**
     * 操作人
     */
    @ApiModelProperty(name = "opUser", value = "操作人")
    @TableField(value = "op_user")
    private String opUser;


    /**
     * 操作时间
     */
    @ApiModelProperty(name = "opTime", value = "操作时间")
    @TableField(value = "op_time")
    private Long opTime;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


}
