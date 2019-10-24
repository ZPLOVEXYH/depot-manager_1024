/**
 * @filename:BRelBillInfo 2019年08月12日
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
 * @Description: 放行指令表体提单信息
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@TableName("b_rel_bill_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRelBillInfo extends Model<BRelBillInfo> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 表头ID
     */
    @ApiModelProperty(name = "relReportHeadId", value = "表头ID")
    @TableField(value = "rel_report_head_id")
    private String relReportHeadId;
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
     * H2000运抵编号
     */
    @ApiModelProperty(name = "h2000ArriveNo", value = "H2000运抵编号")
    @TableField(value = "h2000_arrive_no")
    private String h2000ArriveNo;
    /**
     * 放行方式
     */
    @ApiModelProperty(name = "relType", value = "放行方式")
    @TableField(value = "rel_type")
    private String relType;
    /**
     * 放行时间
     */
    @ApiModelProperty(name = "relTime", value = "放行时间")
    @TableField(value = "rel_time")
    private Long relTime;
    /**
     * 件数
     */
    @ApiModelProperty(name = "packNo", value = "件数")
    @TableField(value = "pack_no")
    private String packNo;
    /**
     * 重量
     */
    @ApiModelProperty(name = "grossWt", value = "重量")
    @TableField(value = "gross_wt")
    private String grossWt;
    /**
     * 卸货地代码
     */
    @ApiModelProperty(name = "dischargePlace", value = "卸货地代码")
    @TableField(value = "discharge_place")
    private String dischargePlace;

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
