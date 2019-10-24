package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.ContaOptionType;
import cn.samples.depot.common.model.DropBoxStatus;
import cn.samples.depot.common.model.GoodsConfirmStatus;
import cn.samples.depot.web.cz.mapper.BDropBoxConfirmMapper;
import cn.samples.depot.web.cz.service.BDropBoxConfirmService;
import cn.samples.depot.web.cz.service.BDropBoxPlanService;
import cn.samples.depot.web.cz.service.BGoodsArriveConfirmService;
import cn.samples.depot.web.cz.service.CStationAreaPositionsService;
import cn.samples.depot.web.cz.service.event.ContaStatusEvent;
import cn.samples.depot.web.entity.BDropBoxConfirm;
import cn.samples.depot.web.entity.BDropBoxPlan;
import cn.samples.depot.web.entity.BGoodsArriveConfirm;
import cn.samples.depot.web.service.UserService;
import cn.samples.depot.web.service.event.EventEmitter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author majunzi
 * @Date 2019/8/8
 * @Description 落箱确认表
 **/
@Service
public class BDropBoxConfirmServiceImpl extends ServiceImpl<BDropBoxConfirmMapper, BDropBoxConfirm> implements BDropBoxConfirmService {
    @Autowired
    BDropBoxPlanService planService;
    @Autowired
    CStationAreaPositionsService positionsService;
    @Autowired
    BGoodsArriveConfirmService goodsArriveConfirmService;
    @Autowired
    UserService userService;
    @Autowired
    EventEmitter emitter;

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 落箱确认 1.校验状态为 待落箱 2.占用堆位，释放锁定 3.修改落箱计划和落箱确认的状态 （落箱计划和落箱确认的id一一对应）
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(BDropBoxConfirm bDropBoxConfirm) throws BizException {
        BDropBoxConfirm confirm = getById(bDropBoxConfirm.getId());
        String entryStatus = getEntryStatus(confirm);
        //校验状态
        if (!DropBoxStatus.PRE_DROP.getValue().equals(confirm.getStatus()) || !GoodsConfirmStatus.GOODS_ARRIVAL.getValue().equals(entryStatus))
            throw new BizException(String.format("仅状态为[%s]且到货状态为[%s] 可以[落箱确认]", DropBoxStatus.PRE_DROP.getTitle(), GoodsConfirmStatus.GOODS_ARRIVAL.getTitle()));
        // 占用推位，释放锁定
        positionsService.onDropBoxConfirm(confirm.getStationAreaCode(), confirm.getStationAreaPositionCode(),
                bDropBoxConfirm.getStationAreaCode(), bDropBoxConfirm.getStationAreaPositionCode(),
                confirm.getContainerNo(), confirm.getContaModelName());
        //修改落箱计划和落箱确认状态
        updateById(buildBoxConfirm(confirm, bDropBoxConfirm));
        planService.updateById(BDropBoxPlan.builder().id(bDropBoxConfirm.getId()).status(DropBoxStatus.DROPPED.getValue()).build());
        //记录集装箱历史记录
        emitter.emit(new ContaStatusEvent(confirm, ContaOptionType.DROPBOX));
    }

    private String getEntryStatus(BDropBoxConfirm confirm) {
        if (!GoodsConfirmStatus.GOODS_ARRIVAL.getValue().equals(confirm.getEntryStatus())) {
            //未到货，查一遍 货到确认
            BGoodsArriveConfirm goodsArriveConfirm = goodsArriveConfirmService.getLastContaNo(confirm.getContainerNo());
            if (null != goodsArriveConfirm && GoodsConfirmStatus.GOODS_ARRIVAL.getValue().equals(goodsArriveConfirm.getStatus())) {
                confirm.setEntryStatus(GoodsConfirmStatus.GOODS_ARRIVAL.getValue());
            }
        }
        return confirm.getEntryStatus();
    }

    private BDropBoxConfirm buildBoxConfirm(BDropBoxConfirm confirm, BDropBoxConfirm bDropBoxConfirm) {
        confirm.setStationAreaCode(bDropBoxConfirm.getStationAreaCode());
        confirm.setStationAreaPositionCode(bDropBoxConfirm.getStationAreaPositionCode());
        confirm.setStatus(DropBoxStatus.DROPPED.getValue());
        confirm.setOpUser(userService.currentUserName());
        confirm.setOpTime(System.currentTimeMillis());
        return confirm;
    }

}