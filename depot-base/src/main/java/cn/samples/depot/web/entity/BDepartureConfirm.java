/**
 * @filename:BDepartureConfirm 2019年08月21日
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
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 发车确认表
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月21日
 * @Version: V1.0
 */
@TableName("b_departure_confirm")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BDepartureConfirm extends Model<BDepartureConfirm> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
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
     * 发车时间
     */
    @ApiModelProperty(name = "departureTime", value = "发车时间")
    @TableField(value = "departure_time")
    @JsonView(View.CREATE.class)
    private Long departureTime;
    /**
     * 运输工具名称
     */
    @ApiModelProperty(name = "shipName", value = "运输工具名称")
    @TableField(value = "ship_name")
    @JsonView(View.CREATE.class)
    private String shipName;
    /**
     * 车次
     */
    @ApiModelProperty(name = "voyageNo", value = "车次")
    @TableField(value = "voyage_no")
    @JsonView(View.CREATE.class)
    private String voyageNo;
    /**
     * 状态
     */
    @ApiModelProperty(name = "status", value = "状态")
    @TableField(value = "status")
    private String status;
    /**
     * 操作人
     */
    @ApiModelProperty(name = "opUser", value = "操作人")
    @TableField(value = "op_user")
    private String opUser;
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

        interface CREATE extends View {
            //新建
        }
    }


}
