/**
 * @filename:BRailwayTallyDelBillInfo 2019年08月12日
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
 * @Description: 铁路进口理货作废报文表体
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@TableName("b_railway_tally_del_bill_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRailwayTallyDelBillInfo extends Model<BRailwayTallyDelBillInfo> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 表头ID
     */
    @ApiModelProperty(name = "railwayTallyReportHeadId", value = "表头ID")
    @TableField(value = "railway_tally_report_head_id")
    private String railwayTallyReportHeadId;
    /**
     * 提单号
     */
    @ApiModelProperty(name = "billNo", value = "提单号")
    @TableField(value = "bill_no")
    private String billNo;
    /**
     * 车皮号
     */
    @ApiModelProperty(name = "carriageNo", value = "车皮号")
    @TableField(value = "carriage_no")
    private String carriageNo;
    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    @TableField(value = "conta_no")
    private String contaNo;
    /**
     * 箱型
     */
    @ApiModelProperty(name = "contaType", value = "箱型")
    @TableField(value = "conta_type")
    private String contaType;
    /**
     * 封志号
     */
    @ApiModelProperty(name = "sealNo", value = "封志号")
    @TableField(value = "seal_no")
    private String sealNo;
    /**
     * 审核状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态(01 待申报 02 申报海关 03 审核通过 04 审核不通过)")
    @TableField(value = "audit_status")
    private String auditStatus;
    /**
     * 件数
     */
    @ApiModelProperty(name = "packNo", value = "件数")
    @TableField(value = "pack_no")
    private Integer packNo;
    /**
     * 重量
     */
    @ApiModelProperty(name = "grossWt", value = "重量")
    @TableField(value = "gross_wt")
    private Double grossWt;
    /**
     * 备注
     */
    @ApiModelProperty(name = "notes", value = "备注")
    @TableField(value = "notes")
    private String notes;
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
