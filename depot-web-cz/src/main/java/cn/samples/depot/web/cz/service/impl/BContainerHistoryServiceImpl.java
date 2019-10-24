package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.model.ContaOptionType;
import cn.samples.depot.common.utils.UniqueIdUtil;
import cn.samples.depot.web.cz.service.BContainerHistoryService;
import cn.samples.depot.web.entity.BContainerHistory;
import cn.samples.depot.web.entity.BContainerInfo;
import cn.samples.depot.web.mapper.BContainerHistoryMapper;
import cn.samples.depot.web.service.impl.CRUDServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/9/23
 * @Description 集装箱状态历史记录
 **/
@Service
@Slf4j
public class BContainerHistoryServiceImpl extends CRUDServiceImpl<BContainerHistoryMapper, BContainerHistory> implements BContainerHistoryService {


    @Override
    public List<BContainerHistory> listHistory(String contaId) {
        return list(new LambdaQueryWrapper<BContainerHistory>().eq(BContainerHistory::getContainerId, contaId).orderByDesc(BContainerHistory::getCreateTime));
    }

    /**
     * @Author majunzi
     * @Date 2019/9/24
     * @Description 根据集装箱状态信息，创建一条带基本数据的 集装箱历史记录
     **/
    @Override
    public BContainerHistory getNew(BContainerInfo contaInfo, ContaOptionType workType) {
        return BContainerHistory.builder()
                .id(UniqueIdUtil.getUUID())
                .containerId(contaInfo.getId())
                .contaNo(contaInfo.getContaNo())
                .contaType(contaInfo.getContaType())
                .status(workType.getStatus().getValue())
                .workType(workType.getValue())
                .createTime(System.currentTimeMillis())
                .build();
    }

    @Override
    public void beforeSave(BContainerHistory history) throws Throwable {
        log.info(history.toString());
    }
}