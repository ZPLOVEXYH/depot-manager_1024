/**
 * @filename:BRelReportHead 2019年08月12日
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
 * @Description: 放行指令表头
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@TableName("b_rel_report_head")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRelReportHead extends Model<BRelReportHead> {

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
     * 报文类型(放行WLJK_REL,装车WLJK_TLA)
     */
    @ApiModelProperty(name = "messageType", value = "报文类型(放行WLJK_REL,装车WLJK_TLA)")
    @TableField(value = "message_type")
    private String messageType;
    /**
     * 海关代码
     */
    @ApiModelProperty(name = "customsCode", value = "海关代码")
    @TableField(value = "customs_code")
    private String customsCode;
    /**
     * 进出口标记（I 进口,E 出口）
     */
    @ApiModelProperty(name = "iEFlag", value = "进出口标记（I 进口,E 出口）")
    @TableField(value = "i_e_flag")
    private String iEFlag;
    /**
     * 场站编码(接收者)
     */
    @ApiModelProperty(name = "stationCode", value = "场站编码(接收者)")
    @TableField(value = "station_code")
    private String stationCode;
    /**
     * 发送者
     */
    @ApiModelProperty(name = "sendId", value = "发送者")
    @TableField(value = "send_id")
    private String sendId;
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
