package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.model.ContaOptionType;
import cn.samples.depot.web.bean.stations.position.AreaPositionContaSelect;
import cn.samples.depot.web.cz.mapper.BMoveBoxConfirmMapper;
import cn.samples.depot.web.cz.service.BMoveBoxConfirmService;
import cn.samples.depot.web.cz.service.CStationAreaPositionsService;
import cn.samples.depot.web.cz.service.event.ContaStatusEvent;
import cn.samples.depot.web.entity.BMoveBoxConfirm;
import cn.samples.depot.web.service.UserService;
import cn.samples.depot.web.service.event.EventEmitter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author majunzi
 * @Date 2019/8/13
 * @Description 移箱
 **/
@Service
public class BMoveBoxConfirmServiceImpl extends ServiceImpl<BMoveBoxConfirmMapper, BMoveBoxConfirm> implements BMoveBoxConfirmService {

    @Autowired
    CStationAreaPositionsService areaPositionsService;
    @Autowired
    UserService userService;
    @Autowired
    EventEmitter emitter;

    /**
     * @Author majunzi
     * @Date 2019/8/13
     * @Description 新增移箱
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void move(BMoveBoxConfirm bMoveBoxConfirm) throws Throwable {
        //修改 箱型，移前堆存区，移前堆位 创建时间  操作人
        AreaPositionContaSelect areaPositionConta = areaPositionsService.getByContano(bMoveBoxConfirm.getContainerNo());
        if(null!=areaPositionConta){
            bMoveBoxConfirm.setContaModelName(areaPositionConta.getContaType());
            bMoveBoxConfirm.setOldStationAreaCode(areaPositionConta.getAreaCode());
            bMoveBoxConfirm.setOldStationAreaPositionCode(areaPositionConta.getPositioncode());
        }
        bMoveBoxConfirm.setCreateTime(System.currentTimeMillis());
        bMoveBoxConfirm.setOpUser(userService.currentUserName());
        save(bMoveBoxConfirm);
        //移动前 堆位->释放占用 ;移动后 堆位->占用
        areaPositionsService.onMoveBoxConfirm(bMoveBoxConfirm);
        emitter.emit(new ContaStatusEvent(bMoveBoxConfirm, ContaOptionType.MOVE_BOX));
    }
}