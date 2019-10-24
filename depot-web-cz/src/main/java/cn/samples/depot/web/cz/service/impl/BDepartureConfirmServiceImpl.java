package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.ContaOptionType;
import cn.samples.depot.web.bean.departureconfirm.BDepartureConfirmVo;
import cn.samples.depot.web.cz.mapper.BDepartureConfirmMapper;
import cn.samples.depot.web.cz.service.BDepartureConfirmService;
import cn.samples.depot.web.cz.service.event.ContaStatusEvent;
import cn.samples.depot.web.entity.BDepartureConfirm;
import cn.samples.depot.web.service.UserService;
import cn.samples.depot.web.service.event.EventEmitter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 发车确认
 **/
@Service
public class BDepartureConfirmServiceImpl extends ServiceImpl<BDepartureConfirmMapper, BDepartureConfirm> implements BDepartureConfirmService {
    @Autowired
    UserService userService;
    @Autowired
    EventEmitter emitter;

    @Override
    @Transactional
    public void save(BDepartureConfirmVo vo) throws BizException {
        saveBefore(vo);
        List<BDepartureConfirm> confirms = vo.buildDepartureConfirm(userService.currentUserName());
        saveBatch(confirms);
        for (BDepartureConfirm confirm : confirms) {
            emitter.emit(new ContaStatusEvent(confirm, ContaOptionType.DEPARTURE));
        }
    }

    private void saveBefore(BDepartureConfirmVo vo) throws BizException {
        if (null == vo) throw new BizException("发车时间不能为空");
        if (null == vo.getDepartureTime() || vo.getDepartureTime() <= 0) throw new BizException("发车时间不能为空");
        if (CollectionUtils.isEmpty(vo.getContaList())) throw new BizException("集装箱不能为空");
    }
}