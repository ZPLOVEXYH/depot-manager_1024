/**
 * @filename:BExRailwayConta 2019年08月21日
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
 * @Description: 铁路运抵申报报文表集装箱信息
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月21日
 * @Version: V1.0
 */
@TableName("b_ex_railway_conta")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BExRailwayConta extends Model<BExRailwayConta> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 表头ID
     */
    @ApiModelProperty(name = "exRailwayReportHeadId", value = "表头ID")
    @TableField(value = "ex_railway_report_head_id")
    private String exRailwayReportHeadId;
    /**
     * 运抵信息ID
     */
    @ApiModelProperty(name = "exRailwayListId", value = "运抵信息ID")
    @TableField(value = "ex_railway_list_id")
    private String exRailwayListId;
    /**
     * 运抵编号
     */
    @ApiModelProperty(name = "arriveNo", value = "运抵编号")
    @TableField(value = "arrive_no")
    private String arriveNo;
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
