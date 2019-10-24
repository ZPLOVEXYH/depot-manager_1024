/**
 * @filename:BRelShipInfo 2019年08月12日
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
 * @Description: 放行指令表体运输工具信息
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@TableName("b_rel_ship_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRelShipInfo extends Model<BRelShipInfo> {

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
     * 运输工具代码
     */
    @ApiModelProperty(name = "shipId", value = "运输工具代码")
    @TableField(value = "ship_id")
    private String shipId;
    /**
     * 进出口标记
     */
    @ApiModelProperty(name = "iEFlag", value = "进出口标记")
    @TableField(value = "i_e_flag")
    private String iEFlag;
    /**
     * 运输工具名称
     */
    @ApiModelProperty(name = "shipNameEn", value = "运输工具名称")
    @TableField(value = "ship_name_en")
    private String shipNameEn;
    /**
     * 航线班次
     */
    @ApiModelProperty(name = "voyageNo", value = "航线班次")
    @TableField(value = "voyage_no")
    private String voyageNo;
    /**
     * 航线标记
     */
    @ApiModelProperty(name = "lineFlag", value = "航线标记")
    @TableField(value = "line_flag")
    private String lineFlag;
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
