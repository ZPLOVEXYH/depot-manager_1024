/**
 * @filename:CEnterprisesMapper 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.mapper;

import cn.samples.depot.web.entity.CEnterprises;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 企业信息表——Mapper
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
@Mapper
public interface CEnterprisesMapper extends BaseMapper<CEnterprises> {

}
