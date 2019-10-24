/**
 * @filename:BContainerHistory 2019年09月20日
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
 * @Description: 集装箱状态历史记录表
 * @Author: ZhangPeng
 * @CreateDate: 2019年09月20日
 * @Version: V1.0
 */
@TableName("b_container_history")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BContainerHistory extends Model<BContainerHistory> {

    public interface View extends CRUDView {
        interface Table extends CRUDView, CRUDView.Table {//表格
        }

        interface Form extends CRUDView.Table {//表单
        }

        interface SELECT extends View { //下拉
        }
    }

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 箱状态表ID
     */
    @ApiModelProperty(name = "containerId", value = "箱状态表ID")
    @TableField(value = "container_id")
    private String containerId;


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
     * 箱状态
     */
    @ApiModelProperty(name = "status", value = "箱状态")
    @TableField(value = "status")
    private String status;


    /**
     * 作业类型
     */
    @ApiModelProperty(name = "workType", value = "作业类型")
    @TableField(value = "work_type")
    private String workType;


    /**
     * 计划时间
     */
    @ApiModelProperty(name = "planTime", value = "计划时间")
    @TableField(value = "plan_time")
    private Long planTime;


    /**
     * 作业时间
     */
    @ApiModelProperty(name = "workTime", value = "作业时间")
    @TableField(value = "work_time")
    private Long workTime;


    /**
     * 出入场工具
     */
    @ApiModelProperty(name = "shipName", value = "出入场工具")
    @TableField(value = "ship_name")
    private String shipName;


    /**
     * 车次
     */
    @ApiModelProperty(name = "voyageNo", value = "车次")
    @TableField(value = "voyage_no")
    private String voyageNo;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


}
