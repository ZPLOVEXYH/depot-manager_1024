/**
 * @filename:BRailwayLoadDelConta 2019年08月20日
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
 * @Description: 装车记录单作废报文表集装箱信息
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@TableName("b_railway_load_del_conta")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRailwayLoadDelConta extends Model<BRailwayLoadDelConta> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 表头ID
     */
    @ApiModelProperty(name = "railwayLoadDelReportHeadId", value = "表头ID")
    @TableField(value = "railway_load_del_report_head_id")
    private String railwayLoadDelReportHeadId;
    /**
     * 提单号
     */
    @ApiModelProperty(name = "billNo", value = "提单号")
    @TableField(value = "bill_no")
    private String billNo;
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
     * 封志号
     */
    @ApiModelProperty(name = "sealNo", value = "封志号")
    @TableField(value = "seal_no")
    private String sealNo;
    /**
     * 封志数量
     */
    @ApiModelProperty(name = "sealNum", value = "封志数量")
    @TableField(value = "seal_num")
    private Integer sealNum;
    /**
     * 车皮号
     */
    @ApiModelProperty(name = "carriageNo", value = "车皮号")
    @TableField(value = "carriage_no")
    private String carriageNo;
    /**
     * 备注
     */
    @ApiModelProperty(name = "notes", value = "备注")
    @TableField(value = "notes")
    private String notes;
    /**
     * 审核状态
     */
    @ApiModelProperty(name = "auditStatus", value = "审核状态")
    @TableField(value = "audit_status")
    private String auditStatus;
    /**
     * 审核时间
     */
    @ApiModelProperty(name = "auditTime", value = "审核时间")
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
