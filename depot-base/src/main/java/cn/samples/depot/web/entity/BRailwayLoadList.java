/**
 * @filename:BRailwayLoadList 2019年08月20日
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
 * @Description: 装车记录单申报报文表体运抵单信息
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月20日
 * @Version: V1.0
 */
@TableName("b_railway_load_list")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRailwayLoadList extends Model<BRailwayLoadList> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 表头ID
     */
    @ApiModelProperty(name = "railwayLoadReportHeadId", value = "表头ID")
    @TableField(value = "railway_load_report_head_id")
    private String railwayLoadReportHeadId;
    /**
     * 集装箱表ID
     */
    @ApiModelProperty(name = "railwayLoadReportContaId", value = "集装箱表ID")
    @TableField(value = "railway_load_report_conta_id")
    private String railwayLoadReportContaId;
    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    @TableField(value = "conta_no")
    private String contaNo;
    /**
     * 运抵编号
     */
    @ApiModelProperty(name = "arriveNo", value = "运抵编号")
    @TableField(value = "arrive_no")
    private String arriveNo;
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
