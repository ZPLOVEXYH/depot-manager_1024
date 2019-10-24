package cn.samples.depot.web.bean.departureconfirm;

import cn.samples.depot.web.bean.stations.position.ContaBase;
import cn.samples.depot.web.entity.BDepartureConfirm;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author majunzi 发车确认 新增，保存对象
 * @Description
 * @Date 2019-08-21 15:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BDepartureConfirmVo {
    /**
     * 发车时间
     */
    @ApiModelProperty(name = "departureTime", value = "发车时间")
    private Long departureTime;
    /**
     * 运输工具名称
     */
    @ApiModelProperty(name = "shipName", value = "运输工具名称")
    private String shipName;
    /**
     * 车次
     */
    @ApiModelProperty(name = "voyageNo", value = "车次")
    private String voyageNo;

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 集装箱集合
     **/
    @ApiModelProperty(name = "contaList", value = "集装箱集合")
    List<ContaBase> contaList;

    public List<BDepartureConfirm> buildDepartureConfirm(String opUser) {
        return this.contaList.stream().map(contaBase -> {
            return BDepartureConfirm.builder()
                    .contaNo(contaBase.getContaNo())
                    .contaType(contaBase.getContaType())
                    .departureTime(this.departureTime)
                    .shipName(this.shipName)
                    .voyageNo(this.voyageNo)
                    .status(DepartureStatus.LOADED.getValue())
                    .opUser(opUser)
                    .createTime(System.currentTimeMillis())
                    .build();
        }).collect(Collectors.toList());
    }
}
