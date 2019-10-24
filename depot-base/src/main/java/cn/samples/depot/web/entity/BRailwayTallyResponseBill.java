/**
 * @filename:BRailwayTallyResponseBill 2019年08月12日
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
 * @Description: 铁路进口理货申请报文回执表体
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@TableName("b_railway_tally_response_bill")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRailwayTallyResponseBill extends Model<BRailwayTallyResponseBill> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 回执报文编号
     */
    @ApiModelProperty(name = "railwayTallyResponseId", value = "回执报文编号")
    @TableField(value = "railway_tally_response_id")
    private String railwayTallyResponseId;
    /**
     * 报文发送方
     */
    @ApiModelProperty(name = "billNo", value = "报文发送方")
    @TableField(value = "bill_no")
    private String billNo;
    /**
     * 回执类型代码
     */
    @ApiModelProperty(name = "chkFlag", value = "回执类型代码")
    @TableField(value = "chk_flag")
    private String chkFlag;
    /**
     * 回执描述
     */
    @ApiModelProperty(name = "notes", value = "回执描述")
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
