package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.web.cz.mapper.BExRailwayDelContaMapper;
import cn.samples.depot.web.cz.service.BExRailwayDelContaService;
import cn.samples.depot.web.entity.BExRailwayDelConta;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class BExRailwayDelContaServiceImpl extends ServiceImpl<BExRailwayDelContaMapper, BExRailwayDelConta> implements BExRailwayDelContaService {
    @Override
    public Wrapper<BExRailwayDelConta> getHeadIdWrapper(String id) {
        return new LambdaQueryWrapper<BExRailwayDelConta>().eq(BExRailwayDelConta::getExRailwayReportDelHeadId, id);
    }

    @Override
    public Wrapper<BExRailwayDelConta> getListIdWrapper(String id) {
        return new LambdaQueryWrapper<BExRailwayDelConta>().eq(BExRailwayDelConta::getExRailwayDelListId, id);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/26
     * @Description 根据集装箱id，查找运抵作废表头id
     **/
    @Override
    public Set<String> listHeadIdsByPartContaNo(String partContaNo) {
        return listMaps(new LambdaQueryWrapper<BExRailwayDelConta>()
                .select(BExRailwayDelConta::getExRailwayReportDelHeadId)
                .like(BExRailwayDelConta::getContaNo, partContaNo))
                .stream().map(map -> map.get("ex_railway_report_del_head_id").toString())
                .collect(Collectors.toSet());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/29
     * @Description 根据运抵单id，查询集装箱集合
     **/
    @Override
    public List<BExRailwayDelConta> listContas(String listId) {
        return list(getListIdWrapper(listId));

    }
}