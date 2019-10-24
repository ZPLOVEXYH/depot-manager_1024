package cn.samples.depot.web.service;

import cn.samples.depot.web.entity.BContainerHistory;

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

}