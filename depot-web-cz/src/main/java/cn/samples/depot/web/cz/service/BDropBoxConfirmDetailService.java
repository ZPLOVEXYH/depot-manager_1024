package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.BDropBoxConfirmDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/8/23
 * @Description 落箱确认明细
 **/
public interface BDropBoxConfirmDetailService extends IService<BDropBoxConfirmDetail> {

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 根据落箱确认id 列出相关发运计划项集合
     **/
    List<BDropBoxConfirmDetail> listByDropBoxConfirmId(String dropBoxConfirmId);
}