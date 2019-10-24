/**
 * @filename:PCustomsCodeService 2019年7月19日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.PCustomsCode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 海关代码表——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年7月19日
 * @Version: V1.0
 */
public interface PCustomsCodeService extends IService<PCustomsCode> {

    List<Map<String, Object>> select();
}