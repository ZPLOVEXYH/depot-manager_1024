/**
 * @filename:BRailwayLoadDelReportHead 2019年08月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.entity;

import cn.samples.depot.common.model.CRUDView;
import cn.samples.depot.common.model.MessageTypeFlag;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 装车记录单作废报文表头
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@TableName("b_railway_load_del_report_head")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRailwayLoadDelReportHead extends Model<BRailwayLoadDelReportHead> {

    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    @JsonView(BRailwayLoadDelReportHead.View.Table.class)
    private String id;
    /**
     * 报文编号
     */
    @ApiModelProperty(name = "messageId", value = "报文编号")
    @TableField(value = "message_id")
    @JsonView(BRailwayLoadDelReportHead.View.Table.class)
    private String messageId;
    /**
     * 申报编号
     */
    @ApiModelProperty(name = "messageAddId", value = "申报编号")
    @TableField(value = "message_add_id")
    @JsonView(BRailwayLoadDelReportHead.View.Form.class)
    private String messageAddId;
    /**
     * 报文类型
     */
    @ApiModelProperty(name = "messageType", value = "报文类型")
    @TableField(value = "message_type")
    private String messageType;

    @TableField(exist = false)
    private String rspMessageType;

    public String getRspMessageType() {
        return MessageTypeFlag.WLJK_TLR.getValue();
    }

    /**
     * 海关代码
     */
    @ApiModelProperty(name = "customsCode", value = "海关代码")
    @TableField(value = "customs_code")
    private String customsCode;

    /**
     * 海关中文名称
     */
    @ApiModelProperty(name = "customsName", value = "海关中文名称")
    @TableField(exist = false)
    @JsonView(value = View.SELECT.class)
    private String customsName;
    /**
     * 卸货地代码中文名称
     */
    @ApiModelProperty(name = "dischargePlaceName", value = "卸货地代码中文名称")
    @TableField(exist = false)
    @JsonView(value = View.SELECT.class)
    private String dischargePlaceName;
    /**
     * 卸货地代码
     */
    @ApiModelProperty(name = "dischargePlace", value = "卸货地代码")
    @TableField(value = "discharge_place")
    private String dischargePlace;
    /**
     * 审核状态
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态")
    @TableField(value = "audit_status")
    private String auditStatus;
    /**
     * 申报时间
     */
    @ApiModelProperty(name = "auditTime", value = "申报时间")
    @TableField(value = "audit_time")
    private Long auditTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }


}
