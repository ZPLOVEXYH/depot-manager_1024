package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.BMoveBoxConfirm;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 移箱确认
 **/
public interface BMoveBoxConfirmService extends IService<BMoveBoxConfirm> {
    /**
     * @Author majunzi
     * @Date 2019/8/23
     * @Description 移箱
     **/
    void move(BMoveBoxConfirm bMoveBoxConfirm) throws Throwable;
}