/**
 * @filename:CStations 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.entity;

import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.common.utils.PageView;
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
 * @Description: 场站表
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@TableName("c_stations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CStations extends Model<CStations> {

    public interface View {
        interface Table extends PageView.View, JsonResult.View {
        }//表格

        interface Form extends JsonResult.View {
        }//表单

        interface SELECT extends JsonResult.View {
        }//下拉
    }

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "id")
    @TableId(value = "id", type = IdType.UUID)
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    private String id;


    /**
     * 场站编码
     */
    @ApiModelProperty(name = "code", value = "场站编码")
    @TableField(value = "code")
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    private String code;


    /**
     * 场站名称
     */
    @ApiModelProperty(name = "name", value = "场站名称")
    @TableField(value = "name")
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    private String name;


    /**
     * 场站类型
     */
    @ApiModelProperty(name = "stationTypeCode", value = "场站类型")
    @TableField(value = "station_type_code")
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    private String stationTypeCode;


    /**
     * 主管海关代码
     */
    @ApiModelProperty(name = "customsCode", value = "主管海关代码")
    @TableField(value = "customs_code")
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    private String customsCode;


    /**
     * 主管海关名称
     */
    @ApiModelProperty(name = "customsName", value = "主管海关名称")
    @TableField(value = "customs_name")
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    private String customsName;

    /**
     * 经营人代码
     */
    @ApiModelProperty(name = "operatorCode", value = "经营人代码")
    @TableField(value = "operator_code")
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    private String operatorCode;


    /**
     * 经营人名称
     */
    @ApiModelProperty(name = "operatorName", value = "经营人名称")
    @TableField(value = "operator_name")
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    private String operatorName;


    /**
     * 联系人
     */
    @ApiModelProperty(name = "contact", value = "联系人")
    @TableField(value = "contact")
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    private String contact;


    /**
     * 联系方式
     */
    @ApiModelProperty(name = "contactPhone", value = "联系方式")
    @TableField(value = "contact_phone")
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    private String contactPhone;


    /**
     * 场站地址
     */
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    @ApiModelProperty(name = "address", value = "场站地址")
    @TableField(value = "address")
    private String address;


    /**
     * 是否启用
     */
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    @ApiModelProperty(name = "enable", value = "是否启用")
    @TableField(value = "enable")
    private Integer enable;


    /**
     * 创建时间
     */
    @JsonView({View.Form.class, View.SELECT.class, View.Table.class})
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @TableField(value = "create_time")
    @CreatedDate
    private Long createTime = System.currentTimeMillis();


}
