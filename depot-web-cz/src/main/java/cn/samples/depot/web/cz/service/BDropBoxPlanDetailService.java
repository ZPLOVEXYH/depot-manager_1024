package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.BDropBoxPlanDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 落箱计划明细
 **/
public interface BDropBoxPlanDetailService extends IService<BDropBoxPlanDetail> {

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 根据某一落箱计划 列出相关发运计划项集合
     **/
    List<BDropBoxPlanDetail> listByDropBoxPlanId(String dropBoxPlanId);
}