package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.BDropBoxConfirmDetailMapper;
import cn.samples.depot.web.cz.service.BDropBoxConfirmDetailService;
import cn.samples.depot.web.entity.BDropBoxConfirmDetail;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/8/8
 * @Description 落箱确认明细表(具体发运信息)
 **/
@Service
public class BDropBoxConfirmDetailServiceImpl extends ServiceImpl<BDropBoxConfirmDetailMapper, BDropBoxConfirmDetail> implements BDropBoxConfirmDetailService {
    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description
     **/
    @Override
    public List<BDropBoxConfirmDetail> listByDropBoxConfirmId(String id) {
        return list(new LambdaQueryWrapper<BDropBoxConfirmDetail>().eq(BDropBoxConfirmDetail::getDropBoxConfirmId, id));
    }
}