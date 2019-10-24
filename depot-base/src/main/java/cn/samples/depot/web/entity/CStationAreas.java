/**
 * @filename:CStationAreas 2019年7月19日
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
 * @Description: 堆区表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@TableName("c_station_areas")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CStationAreas extends Model<CStationAreas> {

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
    @JsonView(value = {View.SELECT.class})
    private String id;


    /**
     * 堆区编码
     */
    @ApiModelProperty(name = "code", value = "堆区编码")
    @TableField(value = "code")
    @JsonView(value = {View.SELECT.class})
    private String code;


    /**
     * 堆区名称
     */
    @ApiModelProperty(name = "name", value = "堆区名称")
    @TableField(value = "name")
    @JsonView(value = {View.SELECT.class})
    private String name;


    /**
     * 所属场站
     */
    @ApiModelProperty(name = "stationCode", value = "所属场站")
    @TableField(value = "station_code")
    @JsonView(View.SELECT.class)
    private String stationCode;

    /**
     * 贝
     */
    @ApiModelProperty(name = "row", value = "贝")
    @TableField(value = "row_no")
    @JsonView(View.SELECT.class)
    private Integer rowNo;

    /**
     * 列
     */
    @ApiModelProperty(name = "column", value = "列")
    @TableField(value = "column_no")
    @JsonView(View.SELECT.class)
    private Integer columnNo;

    /**
     * 层
     */
    @ApiModelProperty(name = "layer", value = "层")
    @TableField(value = "layer_no")
    private Integer layerNo;

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
    @JsonView(value = {View.SELECT.class})
    private String remark;


    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


}
