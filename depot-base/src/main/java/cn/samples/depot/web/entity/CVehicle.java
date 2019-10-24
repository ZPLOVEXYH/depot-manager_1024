/**
 * @filename:CVehicle 2019年7月19日
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
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 车辆备案表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@TableName("c_vehicle")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CVehicle extends Model<CVehicle> {

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
    @ApiModelProperty(name = "id", value = "id")
    @TableId(value = "id", type = IdType.UUID)
    @JsonView(View.Table.class)
    private String id;


    /**
     * 车牌号
     */
    @ApiModelProperty(name = "vehicleNumber", value = "车牌号")
    @TableField(value = "vehicle_number")
    @JsonView(View.Table.class)
    private String vehicleNumber;


    /**
     * 车辆类型
     */
    @ApiModelProperty(name = "vehicleTypeCode", value = "车辆类型")
    @TableField(value = "vehicle_type_code")
    @JsonView(View.Table.class)
    private String vehicleTypeCode;


    /**
     * 车牌颜色
     */
    @ApiModelProperty(name = "vehicleColor", value = "车牌颜色")
    @TableField(value = "vehicle_color")
    @JsonView(View.Table.class)
    private String vehicleColor;


    /**
     * 车辆重量
     */
    @ApiModelProperty(name = "weight", value = "车辆重量")
    @TableField(value = "weight")
    @JsonView(View.Table.class)
    private Double weight;


    /**
     * 车主
     */
    @ApiModelProperty(name = "contact", value = "车主")
    @TableField(value = "contact")
    @JsonView(View.Table.class)
    private String contact;


    /**
     * 企业编码
     */
    @ApiModelProperty(name = "enterpriseCode", value = "企业编码")
    @TableField(value = "enterprise_code")
    @JsonView(View.Table.class)
    private String enterpriseCode;


    /**
     * 企业名称
     */
    @ApiModelProperty(name = "enterpriceName", value = "企业名称")
    @TableField(value = "enterprice_name")
    @JsonView(View.Table.class)
    private String enterpriceName;


    /**
     * 联系手机号
     */
    @ApiModelProperty(name = "contactPhone", value = "联系手机号")
    @TableField(value = "contact_phone")
    @JsonView(View.Table.class)
    private String contactPhone;

    /**
     * 身份证号
     */
    @ApiModelProperty(name = "idcard", value = "身份证号")
    @TableField(value = "idcard")
    @JsonView(View.Table.class)
    private String idcard;


    /**
     * 审核状态
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态")
    @TableField(value = "audit_status")
    private String auditStatus;


    /**
     * 审核时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "auditTime", value = "审核时间")
    @TableField(value = "audit_time")
    private Date auditTime;


    /**
     * 审核人
     */
    @ApiModelProperty(name = "auditUser", value = "审核人")
    @TableField(value = "audit_user")
    private String auditUser;


    /**
     * 是否启用
     */
    @ApiModelProperty(name = "enable", value = "是否启用")
    @TableField(value = "enable")
    private Integer enable;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    @JsonView(View.Table.class)
    private Long createTime = System.currentTimeMillis();


}
