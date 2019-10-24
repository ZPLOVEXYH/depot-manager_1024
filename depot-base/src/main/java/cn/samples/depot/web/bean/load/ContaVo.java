package cn.samples.depot.web.bean.load;

import cn.samples.depot.web.entity.BRailwayLoadConta;
import cn.samples.depot.web.entity.BRailwayLoadList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author majunzi
 * @Description 装车记录-集装箱信息
 * @Date 2019-08-21 17:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContaVo {

    BRailwayLoadConta conta;

    List<BRailwayLoadList> arrayList;
}
