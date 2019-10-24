package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.bean.departureconfirm.BDepartureConfirmVo;
import cn.samples.depot.web.entity.BDepartureConfirm;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 发车确认
 **/
public interface BDepartureConfirmService extends IService<BDepartureConfirm> {

    void save(BDepartureConfirmVo bDepartureConfirmVo) throws BizException;
}