package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.model.UseStatus;
import cn.samples.depot.web.bean.stations.position.AreaPositionContaSelect;
import cn.samples.depot.web.bean.stations.position.ContaBase;
import cn.samples.depot.web.cz.mapper.CStationAreaPositionsMapper;
import cn.samples.depot.web.cz.service.CStationAreaPositionsService;
import cn.samples.depot.web.cz.service.CStationAreasService;
import cn.samples.depot.web.cz.service.event.PositionsConstaHistoryEvent;
import cn.samples.depot.web.entity.BMoveBoxConfirm;
import cn.samples.depot.web.entity.CStationAreaPositions;
import cn.samples.depot.web.entity.CStationAreas;
import cn.samples.depot.web.service.event.CommonEvent;
import cn.samples.depot.web.service.event.EventEmitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author majunzi
 * @Date 2019/8/8
 * @Description 堆位
 **/
@Service
public class CStationAreaPositionsServiceImpl extends ServiceImpl<CStationAreaPositionsMapper, CStationAreaPositions> implements CStationAreaPositionsService {


    @Autowired
    private CStationAreasService areasService;
    @Autowired
    private EventEmitter eventEmitter;

    static String SPLIT = "/";

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 落箱计划 安排。锁定堆位
     **/
    @Override
    public void onDropBoxPlanArrangement(String areaCode, String positionCode) throws BizException {
        setPreUsed(areaCode, positionCode);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 落箱确认。落箱确认堆位 占用。落箱计划堆位 释放。
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onDropBoxConfirm(String planAreaCode, String planPositionCode, String confirmAreaCode, String confirmPositionCode, String containerNo, String contaModelName) throws BizException {
        setUsed(confirmAreaCode, confirmPositionCode, containerNo, contaModelName);
        releasePreUsed(planAreaCode, planPositionCode);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 发车确认 堆位释放
     **/
    @Override
    public void onDepartConfirm(String areaCode, String positionCode) throws BizException {
        releaseUsed(areaCode, positionCode);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 移箱确认 移箱前堆位释放，移箱后堆位占用
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMoveBoxConfirm(BMoveBoxConfirm bMoveBoxConfirm) throws BizException {
        releaseUsed(bMoveBoxConfirm.getOldStationAreaCode(), bMoveBoxConfirm.getOldStationAreaPositionCode());
        setUsed(bMoveBoxConfirm.getNewStationAreaCode(), bMoveBoxConfirm.getNewStationAreaPositionCode(), bMoveBoxConfirm.getContainerNo(), bMoveBoxConfirm.getContaModelName());
    }

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 提货通知 堆位占用
     **/
    @Override
    public void onDeliveryNotice(String areaCode, String positionCode, String containerNo, String contaModelName) throws BizException {
        setUsed(areaCode, positionCode, containerNo, contaModelName);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 提货确认 堆位释放
     **/
    @Override
    public void onDeliveryConfirm(String areaCode, String positionCode) throws BizException {
        releaseUsed(areaCode, positionCode);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 锁定堆位  1.校验状态不为占用。2.状态改为锁定，3.增加锁定次数
     **/
    @Override
    public void setPreUsed(String areaCode, String positionCode) throws BizException {
        CStationAreaPositions position = getByCodeAndAreaCode(positionCode, areaCode);
        if (UseStatus.USED.getValue().equals(position.getUsed()))
            throw new BizException(String.format("堆位[%s]已被占用", position.getName()));
        position.setUsed(UseStatus.PRE_USED.getValue());
        position.setPreusedTimes(position.getPreusedTimes() == null ? 1 : position.getPreusedTimes() + 1);
        updateById(position);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 占用堆位 1.校验状态不为占用 2.状态改为占用，设置集装箱号，箱型 3.记录堆位使用日志
     **/
    @Override
    public void setUsed(String areaCode, String positionCode, String containerNo, String contaModelName) throws BizException {
        CStationAreaPositions position = getByCodeAndAreaCode(positionCode, areaCode);
        if (UseStatus.USED.getValue().equals(position.getUsed()))
            throw new BizException(String.format("堆位[%s]已被占用", position.getName()));
        CStationAreaPositions position2 = getOne(new LambdaQueryWrapper<CStationAreaPositions>()
                .eq(CStationAreaPositions::getContaNo, containerNo)
                .ne(CStationAreaPositions::getId, position.getId()));
        if (null != position2)
            throw new BizException(String.format("集装箱[%s]已经在[%s]位了", containerNo, position2.getName()));
        position.setUsed(UseStatus.USED.getValue());
        position.setContaNo(containerNo);
        position.setContaType(contaModelName);
        updateById(position);
        eventEmitter.emit(new PositionsConstaHistoryEvent(position, CommonEvent.EventType.SAVE));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 锁定释放 锁定次数-1,如果锁定次数变为0，且状态为锁定，则状态改为可用
     **/
    @Override
    public void releasePreUsed(String areaCode, String positionCode) throws BizException {
        CStationAreaPositions position = getByCodeAndAreaCode(positionCode, areaCode);
        position.setPreusedTimes(position.getPreusedTimes() - 1);
        if (position.getPreusedTimes() <= 0 && !position.getUsed().equals(UseStatus.USED.getValue()))
            position.setUsed(UseStatus.NOT_USED.getValue());
        updateById(position);
    }

    /**
     * @Author majunzi
     * @Date 2019/8/12
     * @Description 释放占用，查看锁定次数，>0 锁定,<=0：可用， 集装箱号，箱型 置空。
     **/
    @Override
    public void releaseUsed(String areaCode, String positionCode) throws BizException {
        CStationAreaPositions position = getByCodeAndAreaCode(positionCode, areaCode);
        if (position.getPreusedTimes() > 0) position.setUsed(UseStatus.PRE_USED.getValue());
        position.setUsed(UseStatus.NOT_USED.getValue());
        position.setContaNo(null);
        position.setContaType(null);
        //todo majunzi
        updateById(position);
    }


    @Override
    public CStationAreaPositions getByCodeAndAreaCode(String positionCode, String areaCode) throws BizException {
        CStationAreaPositions position = getOne(new LambdaQueryWrapper<CStationAreaPositions>().eq(CStationAreaPositions::getStationAreaCode, areaCode).eq(CStationAreaPositions::getCode, positionCode), true);
        if (null == position)
            throw new BizException(String.format("找不到code[%s],areaCode[%s]对应的堆位", positionCode, areaCode));
        if (Status.DISABLED.getValue().equals(position.getEnable()))
            throw new BizException(String.format("code[%s],areaCode[%s]对应的堆位 未启用", positionCode, areaCode));
        return position;
    }

    @Override
    public int countByStatus(String areaCode, UseStatus useStatus) {
        return count(new LambdaQueryWrapper<CStationAreaPositions>()
                .eq(CStationAreaPositions::getStationAreaCode, areaCode)
                .eq(CStationAreaPositions::getEnable, Status.ENABLED.getValue())
                .eq(CStationAreaPositions::getUsed, useStatus.getValue()));
    }

    @Override
    public List<CStationAreaPositions> listByStatus(String areaCode, UseStatus useStatus) {
        return list(new LambdaQueryWrapper<CStationAreaPositions>()
                .eq(CStationAreaPositions::getStationAreaCode, areaCode)
                .eq(CStationAreaPositions::getEnable, Status.ENABLED.getValue())
                .eq(CStationAreaPositions::getUsed, useStatus.getValue()));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 根据堆区id，获取可用和锁定状态下的堆位集合
     **/
    @Override
    public List<CStationAreaPositions> selectUseable(String areaCode) {
        List<CStationAreaPositions> notUseds = listByStatus(areaCode, UseStatus.NOT_USED);
        notUseds.forEach(notused -> notused.setRemark(UseStatus.NOT_USED.getTitle()));
        List<CStationAreaPositions> preUseds = listByStatus(areaCode, UseStatus.PRE_USED);
        preUseds.forEach(preUse -> preUse.setRemark(UseStatus.PRE_USED.getTitle()));
        notUseds.addAll(preUseds);
        return notUseds;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/21
     * @Description 获取 集装箱列表(箱型箱号) 占用
     **/
    @Override
    public List<ContaBase> selectConta() {
        List<CStationAreaPositions> positions = listUsed();
        return positions.stream().map(position -> {
            return ContaBase.build(position);
        }).collect(Collectors.toList());
    }

    private List<CStationAreaPositions> listUsed() {
        return list(new LambdaQueryWrapper<CStationAreaPositions>()
                .eq(CStationAreaPositions::getEnable, Status.ENABLED.getValue())
                .eq(CStationAreaPositions::getUsed, UseStatus.USED.getValue()));
    }

    /**
     * @Author majunzi
     * @Date 2019/8/13
     * @Description 获取 集装箱列表(包括堆存堆位信息)，占用
     **/
    @Override
    public List<AreaPositionContaSelect> selectAreaPositionConta() {
        List<CStationAreaPositions> positions = listUsed();
        return positions.stream().map(position -> {
            CStationAreas area = null;
            try {
                area = areasService.getByCode(position.getStationAreaCode());

            } catch (BizException be) {
                be.printStackTrace();
            }
            return AreaPositionContaSelect.build(area, position);
        }).collect(Collectors.toList());
    }

    @Override
    public AreaPositionContaSelect getByContano(String contano) throws BizException {
        CStationAreaPositions position = getOne(new LambdaQueryWrapper<CStationAreaPositions>().eq(CStationAreaPositions::getContaNo, contano));
        if(null==position) return null;
        CStationAreas area = areasService.getByCode(position.getStationAreaCode());
        return AreaPositionContaSelect.build(area, position);
    }

    @Override
    public void checkContainer(String contaNo, String contaType) throws BizException {
        CStationAreaPositions position = getOne(new LambdaQueryWrapper<CStationAreaPositions>().eq(CStationAreaPositions::getContaNo, contaNo));
        if (null == position) throw new BizException(String.format("集装箱号[%s]不存在", contaNo));
        if (position.getContaType() == null || !position.getContaType().equals(contaType))
            throw new BizException("集装箱号" + contaNo + "，箱型 " + contaType + "在堆存查询中检索不到");
    }

    @Override
    public List<CStationAreaPositions> getByAreaCode(String areaCode) {
        return list(new LambdaQueryWrapper<CStationAreaPositions>()
                .eq(CStationAreaPositions::getStationAreaCode, areaCode));
    }

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 新增，箱位编码全局唯一，必填字段：堆区编码,贝，列，层
     **/
    @Override
    public void saveT(CStationAreaPositions cStationAreaPositions) throws BizException {
        CStationAreaPositions position = saveBefore(cStationAreaPositions);
        save(position);
    }

    private CStationAreaPositions saveBefore(CStationAreaPositions areaPosiont) throws BizException {
        if (StringUtils.isEmpty(areaPosiont.getStationAreaCode())) throw new BizException("堆区编码不能为空");
        if (StringUtils.isEmpty(areaPosiont.getRow())) throw new BizException("贝不能为空");
        if (StringUtils.isEmpty(areaPosiont.getColumnNo())) throw new BizException("列不能为空");
        if (StringUtils.isEmpty(areaPosiont.getLayer())) throw new BizException("层不能为空");
        CStationAreas area = areasService.getByCode(areaPosiont.getStationAreaCode());
        Integer row = Integer.valueOf(areaPosiont.getRow());
        Integer column = Integer.valueOf(areaPosiont.getColumnNo());
        Integer layer = Integer.valueOf(areaPosiont.getLayer());
        if (row > (area.getRowNo() == null ? 0 : area.getRowNo()))
            throw new BizException(String.format("贝超界，请查证后再操作"));
        if (column > (area.getColumnNo() == null ? 0 : area.getColumnNo()))
            throw new BizException(String.format("列超界，请查证后再操作"));
        if (layer > (area.getLayerNo() == null ? 0 : area.getLayerNo()))
            throw new BizException(String.format("层超界，请查证后再操作"));

        areaPosiont.setCode(area.getCode() + SPLIT + areaPosiont.getRow() + SPLIT + areaPosiont.getColumnNo() + SPLIT + areaPosiont.getLayer());
        if (null != getOne(new LambdaQueryWrapper<CStationAreaPositions>().eq(CStationAreaPositions::getCode, areaPosiont.getCode())))
            throw new BizException("箱位已经存在");

        CStationAreaPositions position = areaPosiont.builder().build();
        BeanUtils.copyProperties(areaPosiont, position, "id");
        position.setCreateTime(System.currentTimeMillis());
        return position;
    }
}