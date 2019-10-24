/**
 * @filename:BGoodsArriveConfirmService 2019年08月01日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.utils.JsonResult;
import cn.samples.depot.web.entity.BGoodsArriveConfirm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 货到确认表——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月01日
 * @Version: V1.0
 */
public interface BGoodsArriveConfirmService extends IService<BGoodsArriveConfirm> {


    boolean goodsConfirm(BGoodsArriveConfirm bGoodsArriveConfirm);

    /**
     * @Author majunzi
     * @Date 2019/9/26
     * @Description 根据集装箱号，获取最新的货到确认记录
     **/
    BGoodsArriveConfirm getLastContaNo(String containerNo);

    JsonResult updateBatch(String[] ids, BGoodsArriveConfirm bGoodsArriveConfirm);
}