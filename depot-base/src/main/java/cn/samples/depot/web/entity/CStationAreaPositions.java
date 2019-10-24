/**
 * @filename:CStationAreaPositions 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.entity;

import cn.samples.depot.common.model.CRUDView;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

/**
 * @Description: 箱位表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@TableName("c_station_area_positions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CStationAreaPositions extends Model<CStationAreaPositions> {

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
    @ApiModelProperty(name = "id", value = "id")
    @TableId(value = "id", type = IdType.UUID)
    @JsonView(View.Table.class)
    private String id;


    /**
     * 堆位编码
     */
    @ApiModelProperty(name = "code", value = "堆位编码")
    @TableField(value = "code")
    @JsonView({View.SELECT.class, View.Table.class})
    private String code;


    /**
     * 堆位编码名称
     */
    @ApiModelProperty(name = "name", value = "堆位编码名称")
    @TableField(value = "name")
    @JsonView({View.SELECT.class, View.Table.class})
    private String name;


    /**
     * 所属场区
     */
    @ApiModelProperty(name = "stationAreaCode", value = "所属场区")
    @TableField(value = "station_area_code")
    @JsonView(View.Table.class)
    private String stationAreaCode;

    /**
     * 所属场区名称
     */
    @ApiModelProperty(name = "stationAreaName", value = "所属场区名称")
    @TableField(exist = false)
    @JsonView(View.Table.class)
    private String stationAreaName;

    /**
     * 行
     */
    @ApiModelProperty(name = "row", value = "行")
    @TableField(value = "row_no")
    @JsonView(View.Table.class)
    private String row;

    /**
     * 列
     */
    @ApiModelProperty(name = "columnNo", value = "列")
    @TableField(value = "column_no")
    @JsonView(View.Table.class)
    private String columnNo;

    /**
     * 层
     */
    @ApiModelProperty(name = "layer", value = "层")
    @TableField(value = "layer_no")
    @JsonView(View.Table.class)
    private String layer;


    /**
     * 是否被占用(0未占用 1预占用 2已占用)
     */
    @ApiModelProperty(name = "used", value = "位置是否被占用（0未占用、1预占用、2已占用）")
    @TableField(value = "used")
    private Integer used;

    /**
     * 锁定次数
     */
    @ApiModelProperty(name = "preusedTimes", value = "锁定次数")
    @TableField(value = "preused_times")
    private Integer preusedTimes;

    /**
     * 集装箱编号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱编号")
    @TableField(value = "conta_no", strategy = FieldStrategy.IGNORED)
    private String contaNo;
    /**
     * 箱型
     */
    @ApiModelProperty(name = "contaType", value = "箱型")
    @TableField(value = "conta_type", strategy = FieldStrategy.IGNORED)
    private String contaType;

    public Integer getPreusedTimes() {
        if (null == preusedTimes) {
            preusedTimes = 0;
        }
        return preusedTimes;
    }

    /**
     * 是否启用
     */
    @ApiModelProperty(name = "enable", value = "是否启用")
    @TableField(value = "enable")
    private Integer enable;


    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    @TableField(value = "remark")
    @JsonView({View.SELECT.class, View.Table.class})
    private String remark;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


}
