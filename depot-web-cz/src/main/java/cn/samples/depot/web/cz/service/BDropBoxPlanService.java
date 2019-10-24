package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.BDropBoxPlan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 落箱计划
 **/
public interface BDropBoxPlanService extends IService<BDropBoxPlan> {
    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 落箱安排
     **/
    void arrangement(BDropBoxPlan bDropBoxPlan) throws Throwable;
}