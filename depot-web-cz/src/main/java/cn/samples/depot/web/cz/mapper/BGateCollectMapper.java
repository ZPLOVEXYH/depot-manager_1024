/**
 * @filename:BGateCollectMapper 2019年08月01日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.mapper;

import cn.samples.depot.web.entity.BGateCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 过卡信息采集表——Mapper
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
@Mapper
public interface BGateCollectMapper extends BaseMapper<BGateCollect> {

}
