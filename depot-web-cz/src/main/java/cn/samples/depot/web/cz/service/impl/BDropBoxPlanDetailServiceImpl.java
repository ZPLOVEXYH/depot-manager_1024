package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.BDropBoxPlanDetailMapper;
import cn.samples.depot.web.cz.service.BDropBoxPlanDetailService;
import cn.samples.depot.web.entity.BDropBoxPlanDetail;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/8/8
 * @Description 落箱计划 发运计划项
 **/
@Service
public class BDropBoxPlanDetailServiceImpl extends ServiceImpl<BDropBoxPlanDetailMapper, BDropBoxPlanDetail> implements BDropBoxPlanDetailService {
    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description
     **/
    @Override
    public List<BDropBoxPlanDetail> listByDropBoxPlanId(String dropBoxPlanId) {
        return list(new LambdaQueryWrapper<BDropBoxPlanDetail>().eq(BDropBoxPlanDetail::getDropBoxPlanId, dropBoxPlanId));
    }
}