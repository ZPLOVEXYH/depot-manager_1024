package cn.samples.depot.web.service.impl;

import cn.samples.depot.web.entity.BContainerHistory;
import cn.samples.depot.web.mapper.BContainerHistoryMapper;
import cn.samples.depot.web.service.BContainerHistoryService;
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


    @Override
    public void beforeSave(BContainerHistory history) throws Throwable {
        log.info(history.toString());
    }
}