/**
 * @filename:BShipmentPlanMapper 2019年7月24日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.mapper;

import cn.samples.depot.web.entity.BShipmentPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 发运计划表——Mapper
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月24日
 * @Version: V1.0
 */
@Mapper
@Repository
public interface BShipmentPlanMapper extends BaseMapper<BShipmentPlan> {


}
