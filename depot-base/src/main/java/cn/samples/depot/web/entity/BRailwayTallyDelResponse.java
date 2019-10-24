/**
 * @filename:BRailwayTallyDelResponse 2019年08月12日
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
 * @Description: 铁路进口理货作废报文回执
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@TableName("b_railway_tally_del_response")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRailwayTallyDelResponse extends Model<BRailwayTallyDelResponse> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 回执报文编号
     */
    @ApiModelProperty(name = "messageId", value = "回执报文编号")
    @TableField(value = "message_id")
    private String messageId;
    /**
     * 申请报文编号
     */
    @ApiModelProperty(name = "declMessageId", value = "申请报文编号")
    @TableField(value = "decl_message_id")
    private String declMessageId;
    /**
     * 报文类型
     */
    @ApiModelProperty(name = "messageType", value = "报文类型")
    @TableField(value = "message_type")
    private String messageType;
    /**
     * 报文发送方
     */
    @ApiModelProperty(name = "sendId", value = "报文发送方")
    @TableField(value = "send_id")
    private String sendId;
    /**
     * 报文发送方中文名称
     */
    @ApiModelProperty(name = "sendName", value = "报文发送方中文名称")
    @TableField(exist = false)
    private String sendName;
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
     * 发送时间
     */
    @ApiModelProperty(name = "sendTime", value = "发送时间")
    @TableField(value = "send_time")
    private Long sendTime;
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
