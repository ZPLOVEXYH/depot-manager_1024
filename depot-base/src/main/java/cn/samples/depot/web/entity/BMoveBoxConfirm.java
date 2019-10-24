/**
 * @filename:BMoveBoxConfirm 2019年08月12日
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
 * @Description: 移箱确认表
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@TableName("b_move_box_confirm")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BMoveBoxConfirm extends Model<BMoveBoxConfirm> {

    /**
     * 审核日志id
     */
    @ApiModelProperty(name = "id", value = "id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "containerNo", value = "集装箱号")
    @TableField(value = "container_no")
    private String containerNo;
    /**
     * 集装箱尺寸
     */
    @ApiModelProperty(name = "contaModelName", value = "集装箱尺寸")
    @TableField(value = "conta_model_name")
    private String contaModelName;
    /**
     * 原堆区
     */
    @ApiModelProperty(name = "oldStationAreaCode", value = "原堆区")
    @TableField(value = "old_station_area_code")
    private String oldStationAreaCode;
    /**
     * 原堆位
     */
    @ApiModelProperty(name = "oldStationAreaPositionCode", value = "原堆位")
    @TableField(value = "old_station_area_position_code")
    private String oldStationAreaPositionCode;
    /**
     * 移动后堆区
     */
    @ApiModelProperty(name = "newStationAreaCode", value = "移动后堆区")
    @TableField(value = "new_station_area_code")
    private String newStationAreaCode;
    /**
     * 移动后堆位
     */
    @ApiModelProperty(name = "newStationAreaPositionCode", value = "移动后堆位")
    @TableField(value = "new_station_area_position_code")
    private String newStationAreaPositionCode;
    /**
     * 操作人
     */
    @ApiModelProperty(name = "opUser", value = "操作人")
    @TableField(value = "op_user")
    private String opUser;
    /**
     * 操作时间
     */
    @ApiModelProperty(name = "opTime", value = "操作时间")
    @TableField(value = "op_time")
    private Long opTime;
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
