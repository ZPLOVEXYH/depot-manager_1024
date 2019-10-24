/**
 * @filename:BContainerHistoryMapper 2019年09月20日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.mapper;

import cn.samples.depot.web.entity.BContainerHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 集装箱状态历史记录表——Mapper
 * @Author: ZhangPeng
 * @CreateDate: 2019年09月20日
 * @Version: V1.0
 */
@Mapper
public interface BContainerHistoryMapper extends BaseMapper<BContainerHistory> {

}
