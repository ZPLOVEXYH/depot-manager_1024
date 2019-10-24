/**
 * @filename:BRelFormInfo 2019年08月12日
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
 * @Description: 放行指令表体单证信息
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@TableName("b_rel_form_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRelFormInfo extends Model<BRelFormInfo> {

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
     * 单证ID
     */
    @ApiModelProperty(name = "formId", value = "单证ID")
    @TableField(value = "form_id")
    private String formId;
    /**
     * 单证类型
     */
    @ApiModelProperty(name = "formType", value = "单证类型")
    @TableField(value = "form_type")
    private String formType;
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
