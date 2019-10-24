/**
 * @filename:BRelContaInfo 2019年08月12日
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
 * @Description: 放行指令表体集装箱信息
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@TableName("b_rel_conta_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BRelContaInfo extends Model<BRelContaInfo> {

    /**
     *
     */
    @ApiModelProperty(name = "id", value = "")
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 表头ID
     */
    @ApiModelProperty(name = "relHeadId", value = "表头ID")
    @TableField(value = "rel_head_id")
    private String relHeadId;
    /**
     * 车皮号
     */
    @ApiModelProperty(name = "carriageNo", value = "车皮号")
    @TableField(value = "carriage_no")
    private String carriageNo;
    /**
     * 集装箱号
     */
    @ApiModelProperty(name = "contaNo", value = "集装箱号")
    @TableField(value = "conta_no")
    private String contaNo;
    /**
     * 箱放行方式
     */
    @ApiModelProperty(name = "relType", value = "箱放行方式")
    @TableField(value = "rel_type")
    private String relType;
    /**
     * 箱放行时间
     */
    @ApiModelProperty(name = "relTime", value = "箱放行时间")
    @TableField(value = "rel_time")
    private Long relTime;
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
