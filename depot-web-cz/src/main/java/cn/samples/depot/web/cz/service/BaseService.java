/**
 * @filename:BDepartureConfirmService 2019年08月21日
 * @project depot-manager  V1.0
 * Copyright(c) 2018 ZhangPeng Co. Ltd.
 * All right reserved.
 */
package cn.samples.depot.web.cz.service;

import cn.samples.depot.web.entity.xml.RspRailWayBillInfo;
import cn.samples.depot.web.entity.xml.ex.rsp.RspArriveInfo;
import cn.samples.depot.web.entity.xml.load.rsp.RspBillInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 发车确认表——SERVICE
 * @Author: ZhangPeng
 * @CreateDate: 2019年08月21日
 * @Version: V1.0
 */
public interface BaseService {

    void saveResponseHeadXml(Map<String, String> headRspXml);

    void saveResponseBodyXml(List<RspBillInfo> rspBillInfoList, String functinoCode, String headUUID);

    void saveExResponseBodyXml(List<RspArriveInfo> rspArriveInfoList, String functinoCode, String headUUID);

    void saveRailResponseBodyXml(List<RspRailWayBillInfo> rspRailWayBillInfoList, String functinoCode, String headUUID);
}