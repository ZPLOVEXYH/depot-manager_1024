package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.model.ContaOptionType;
import cn.samples.depot.web.entity.BContainerHistory;
import cn.samples.depot.web.entity.BContainerInfo;
import cn.samples.depot.web.service.CRUDService;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/9/23
 * @Description 集装箱状态历史记录
 **/
public interface BContainerHistoryService extends CRUDService<BContainerHistory> {

    /**
     * @Author majunzi
     * @Date 2019/9/23
     * @Description 根据集装箱id，查找历史
     **/
    List<BContainerHistory> listHistory(String contaId);

    /**
     * @Author majunzi
     * @Date 2019/9/24
     * @Description 根据集装箱状态信息，创建一条带基本数据的 集装箱历史记录
     **/
    BContainerHistory getNew(BContainerInfo contaInfo, ContaOptionType workType);
}