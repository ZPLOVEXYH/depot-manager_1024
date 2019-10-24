package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.UseStatus;
import cn.samples.depot.web.bean.stations.position.AreaPositionContaSelect;
import cn.samples.depot.web.bean.stations.position.ContaBase;
import cn.samples.depot.web.entity.BMoveBoxConfirm;
import cn.samples.depot.web.entity.CStationAreaPositions;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author majunzi
 * @Date 2019/8/8
 * @Description 堆位表
 **/
public interface CStationAreaPositionsService extends IService<CStationAreaPositions> {

    CStationAreaPositions getByCodeAndAreaCode(String positionCode, String areaCode) throws BizException;

    AreaPositionContaSelect getByContano(String contano) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 锁定堆位  1.校验状态不为占用。2.状态改为锁定，3.增加锁定次数
     **/
    void setPreUsed(String areaCode, String positionCode) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 占用堆位 1.校验状态不为占用 2.状态改为占用
     **/
    void setUsed(String areaCode, String positionCode, String containerNo, String contaModelName) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 锁定释放 锁定次数-1,如果锁定次数变为0，且状态为锁定，则状态改为可用
     **/
    void releasePreUsed(String areaCode, String positionCode) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 释放占用，查看锁定次数，0：可用，>0 锁定
     **/
    void releaseUsed(String areaCode, String positionCode) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 获取对应堆区，某一状态的的堆位数量
     **/
    int countByStatus(String areaCode, UseStatus useStatus);

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 获取对应堆区，某一状态的的堆位集合
     **/
    List<CStationAreaPositions> listByStatus(String areaId, UseStatus useStatus);

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 获取可用的堆位（可用+锁定）
     **/
    List<CStationAreaPositions> selectUseable(String areaCode);

    /**
     * @Author majunzi
     * @Date 2019/8/13
     * @Description 获取 集装箱列表(包括堆存堆位信息)
     **/
    List<AreaPositionContaSelect> selectAreaPositionConta();

    /**
     * @Author majunzi
     * @Date 2019/8/13
     * @Description 获取 集装箱列表(箱型箱号)
     **/
    List<ContaBase> selectConta();

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 落箱计划 安排。锁定堆位
     **/
    void onDropBoxPlanArrangement(String areaCode, String positionCode) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 落箱确认。落箱确认堆位 占用。落箱计划堆位 释放。
     **/
    void onDropBoxConfirm(String planAreaCode, String planPositionCode, String confirmAreaCode, String confirmPositionCode, String containerNo, String contaModelName) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 发车确认 堆位释放
     **/
    void onDepartConfirm(String areaCode, String positionCode) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 移箱确认 移箱前堆位释放，移箱后堆位占用
     **/
    void onMoveBoxConfirm(BMoveBoxConfirm bMoveBoxConfirm) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 提货通知 堆位占用
     **/
    void onDeliveryNotice(String areaCode, String positionCode, String containerNo, String contaModelName) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 提货确认 堆位释放
     **/
    void onDeliveryConfirm(String areaCode, String positionCode) throws BizException;


    /**
     * @Author majunzi
     * @Date 2019/8/13
     * @Description 校验集装箱号 是否在对存区，及与箱型配对
     **/
    void checkContainer(String contaNo, String contaType) throws BizException;

    List<CStationAreaPositions> getByAreaCode(String areaCode);

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 新增，箱位编码全局唯一，必填字段：堆区编码,贝，列，层
     **/
    void saveT(CStationAreaPositions cStationAreaPositions) throws BizException;
}