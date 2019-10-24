/**
 * @filename:BGoodsArriveConfirm 2019年08月01日
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
 * @Description: 货到确认表
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
@TableName("b_goods_arrive_confirm")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BGoodsArriveConfirm extends Model<BGoodsArriveConfirm> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     * 审核日志id
     */
    @ApiModelProperty(name = "id", value = "审核日志id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "containerNo", value = "集装箱号")
    @TableField(value = "container_no")
    private String containerNo;


    /**
     * 场站编码
     */
    @ApiModelProperty(name = "stationCode", value = "场站编码")
    @TableField(value = "station_code")
    private String stationCode;

    /**
     * 场站中文名称
     */
    @ApiModelProperty(name = "stationName", value = "场站中文名称")
    @TableField(exist = false)
    private String stationName;


    /**
     * 车牌号
     */
    @ApiModelProperty(name = "vehicleNumber", value = "车牌号")
    @TableField(value = "vehicle_number")
    private String vehicleNumber;


    /**
     * 企业编码
     */
    @ApiModelProperty(name = "enterprisesId", value = "企业编码")
    @TableField(value = "enterprises_id")
    private String enterprisesId;


    /**
     * 企业名称
     */
    @ApiModelProperty(name = "enterprisesName", value = "企业名称")
    @TableField(value = "enterprises_name")
    private String enterprisesName;


    /**
     * 确认类型
     */
    @ApiModelProperty(name = "confirmType", value = "确认类型（00：卡口确认、10：人工确认、99：空值）")
    @TableField(value = "confirm_type")
    private String confirmType;


    /**
     * 货到确认状态
     */
    @ApiModelProperty(name = "status", value = "货到确认状态（00：未到货、10：已到货）")
    @TableField(value = "status")
    private String status;


    /**
     * 货到确认时间
     */
    @ApiModelProperty(name = "entryTime", value = "货到确认时间")
    @TableField(value = "entry_time")
    private Long entryTime;


    /**
     * 确认人
     */
    @ApiModelProperty(name = "confirmUser", value = "确认人")
    @TableField(value = "confirm_user")
    private String confirmUser;


    /**
     * 确认时间
     */
    @ApiModelProperty(name = "confirmTime", value = "确认时间")
    @TableField(value = "confirm_time")
    private Long confirmTime;

    /**
     * 开始货到时间
     */
    @ApiModelProperty(name = "startConfirmTime", value = "开始货到时间")
    @TableField(exist = false)
    private Long startEntryTime;

    /**
     * 结束货到时间
     */
    @ApiModelProperty(name = "endConfirmTime", value = "结束货到时间")
    @TableField(exist = false)
    private Long endEntryTime;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


}
