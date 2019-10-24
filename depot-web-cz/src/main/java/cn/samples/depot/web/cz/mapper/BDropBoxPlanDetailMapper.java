/**
 * @filename:BDropBoxPlanDetailMapper 2019年08月01日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.mapper;

import cn.samples.depot.web.entity.BDropBoxPlanDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 落箱计划明细表(具体发运信息)——Mapper
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
@Mapper
public interface BDropBoxPlanDetailMapper extends BaseMapper<BDropBoxPlanDetail> {

}
