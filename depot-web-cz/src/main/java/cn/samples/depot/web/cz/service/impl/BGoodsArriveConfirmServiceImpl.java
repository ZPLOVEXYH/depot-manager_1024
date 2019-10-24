/**
 * @filename:BGoodsArriveConfirmServiceImpl 2019年08月01日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.model.ConfirmStatus;
import cn.samples.depot.common.model.ContaOptionType;
import cn.samples.depot.common.model.GoodsConfirmStatus;
import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.cz.mapper.BGoodsArriveConfirmMapper;
import cn.samples.depot.web.cz.service.BGoodsArriveConfirmService;
import cn.samples.depot.web.cz.service.event.ContaStatusEvent;
import cn.samples.depot.web.entity.BGoodsArriveConfirm;
import cn.samples.depot.web.service.UserService;
import cn.samples.depot.web.service.event.EventEmitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Description: 货到确认表——SERVICEIMPL
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
@Service
public class BGoodsArriveConfirmServiceImpl extends ServiceImpl<BGoodsArriveConfirmMapper, BGoodsArriveConfirm> implements BGoodsArriveConfirmService {

    @Autowired
    EventEmitter emitter;

    @Autowired
    UserService userService;

    @Override
    public boolean goodsConfirm(BGoodsArriveConfirm bGoodsArriveConfirm) {
        bGoodsArriveConfirm.setStatus(GoodsConfirmStatus.GOODS_ARRIVAL.getValue());
        boolean flag = updateById(bGoodsArriveConfirm);
        //记录集装箱历史
        if (flag) emitter.emit(new ContaStatusEvent(getById(bGoodsArriveConfirm.getId()), ContaOptionType.GOODS_ARR));
        return flag;
    }

    /**
     * @Author majunzi
     * @Date 2019/9/26
     * @Description 根据集装箱号，获取最新的货到确认记录
     **/
    @Override
    public BGoodsArriveConfirm getLastContaNo(String containerNo) {
        return getOne(new LambdaQueryWrapper<BGoodsArriveConfirm>().eq(BGoodsArriveConfirm::getContainerNo, containerNo).orderByDesc(BGoodsArriveConfirm::getCreateTime));
    }

    @Override
    public JsonResult updateBatch(String[] ids, BGoodsArriveConfirm bGoodsArriveConfirm) {
//        UserContext userContext = userService.userContext();
//        CUsers cUsers = userContext.getUser();

        if (null != ids && ids.length > 0) {
            Arrays.stream(ids).forEach(id -> {
                bGoodsArriveConfirm.setStatus(GoodsConfirmStatus.GOODS_ARRIVAL.getValue());
                bGoodsArriveConfirm.setConfirmType(ConfirmStatus.PEOPLE_CONFIRM.getValue());
//                bGoodsArriveConfirm.setConfirmUser(null != cUsers ? cUsers.getId() : "");
                bGoodsArriveConfirm.setConfirmUser("test");
                bGoodsArriveConfirm.setEntryTime(System.currentTimeMillis());
                bGoodsArriveConfirm.setConfirmTime(System.currentTimeMillis());

                this.update(bGoodsArriveConfirm, Wrappers.<BGoodsArriveConfirm>lambdaQuery().eq(BGoodsArriveConfirm::getId, id));

            });
        }

        return JsonResult.success();
    }
}