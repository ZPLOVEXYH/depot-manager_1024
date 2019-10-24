/**
 * @filename:CPositionsConstaHistoryServiceImpl 2019年08月12日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.CPositionsConstaHistoryMapper;
import cn.samples.depot.web.cz.service.CPositionsConstaHistoryService;
import cn.samples.depot.web.cz.service.event.PositionsConstaHistoryEvent;
import cn.samples.depot.web.entity.CPositionsConstaHistory;
import cn.samples.depot.web.entity.CStationAreaPositions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


/**
 * @Author majunzi
 * @Date 2019/8/13
 * @Description 堆位集装箱历史记录表
 **/
@Service
public class CPositionsConstaHistoryServiceImpl extends ServiceImpl<CPositionsConstaHistoryMapper, CPositionsConstaHistory> implements CPositionsConstaHistoryService {

    @EventListener(value = PositionsConstaHistoryEvent.class, condition = "#event.save")
    public void onPositionUsed(PositionsConstaHistoryEvent event) {
        CStationAreaPositions position = event.getSource();
        save(CPositionsConstaHistory.builder()
                .code(position.getCode())
                .contaNo(position.getContaNo())
                .createTime(System.currentTimeMillis())
                .build());
    }
}