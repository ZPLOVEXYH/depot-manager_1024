package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.entity.BDropBoxConfirm;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 落箱确认
 **/
public interface BDropBoxConfirmService extends IService<BDropBoxConfirm> {

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 落箱确认
     **/
    void confirm(BDropBoxConfirm bDropBoxConfirm) throws BizException;
}