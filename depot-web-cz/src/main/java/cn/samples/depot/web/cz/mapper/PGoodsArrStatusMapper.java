/**
 * @filename:PGoodsArrStatusMapper 2019年7月17日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.mapper;

import cn.samples.depot.web.entity.PGoodsArrStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 到货状态表——Mapper
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月17日
 * @Version: V1.0
 */
@Mapper
public interface PGoodsArrStatusMapper extends BaseMapper<PGoodsArrStatus> {

}
