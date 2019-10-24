/**
 * @filename:BRailwayTallyBillInfoMapper 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.mapper;

import cn.samples.depot.web.entity.BRailwayTallyBillInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

/**
 * @Description: 铁路进口理货申请报文表体——Mapper
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月12日
 * @Version: V1.0
 */
@Mapper
@Service
public interface BRailwayTallyBillInfoMapper extends BaseMapper<BRailwayTallyBillInfo> {

}
