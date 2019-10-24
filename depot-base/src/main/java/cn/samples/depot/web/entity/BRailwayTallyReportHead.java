/**
 * @filename:BRailwayTallyReportHead 2019年08月12日
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
 * @Description: 铁路进口理货申请报文表头
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@TableName("b_railway_tally_report_head")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRailwayTallyReportHead extends Model<BRailwayTallyReportHead> {

    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 报文编码
     */
    @ApiModelProperty(name = "messageId", value = "报文编码")
    @TableField(value = "message_id")
    private String messageId;
    /**
     * 报文类型
     */
    @ApiModelProperty(name = "messageType", value = "报文类型")
    @TableField(value = "message_type")
    private String messageType;
    /**
     * 进出口标记（I 进口,E 出口）
     */
    @ApiModelProperty(name = "iEFlag", value = "进出口标记（I 进口,E 出口）")
    @TableField(value = "i_e_flag")
    private String iEFlag;
    /**
     * 海关代码
     */
    @ApiModelProperty(name = "customsCode", value = "海关代码")
    @TableField(value = "customs_code")
    private String customsCode;
    /**
     * 卸货地代码
     */
    @ApiModelProperty(name = "unloadingPlace", value = "卸货地代码")
    @TableField(value = "unloading_place")
    private String unloadingPlace;
    /**
     * 装货地代码
     */
    @ApiModelProperty(name = "loadingPlace", value = "装货地代码")
    @TableField(value = "loading_place")
    private String loadingPlace;
    /**
     * 装卸开始时间
     */
    @ApiModelProperty(name = "actualDatetime", value = "装卸开始时间")
    @TableField(value = "actual_datetime")
    private Long actualDatetime;
    /**
     * 装卸结束时间
     */
    @ApiModelProperty(name = "completedDatetime", value = "装卸结束时间")
    @TableField(value = "completed_datetime")
    private Long completedDatetime;
    /**
     * 审核状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)")
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
