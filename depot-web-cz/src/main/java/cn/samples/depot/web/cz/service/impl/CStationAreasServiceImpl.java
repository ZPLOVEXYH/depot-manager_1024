package cn.samples.depot.web.cz.service.impl;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.common.model.Status;
import cn.samples.depot.common.model.UseStatus;
import cn.samples.depot.web.cz.mapper.CStationAreasMapper;
import cn.samples.depot.web.cz.service.CStationAreaPositionsService;
import cn.samples.depot.web.cz.service.CStationAreasService;
import cn.samples.depot.web.entity.CStationAreas;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author majunzi
 * @Date 2019/8/8
 * @Description 堆区
 **/
@Service
public class CStationAreasServiceImpl extends ServiceImpl<CStationAreasMapper, CStationAreas> implements CStationAreasService {

    @Autowired
    CStationAreaPositionsService positionsService;

    @Override
    public List<Map<String, Object>> select() {
        return listMaps(new LambdaQueryWrapper<CStationAreas>().select(CStationAreas::getId, CStationAreas::getCode, CStationAreas::getName, CStationAreas::getRowNo, CStationAreas::getColumnNo, CStationAreas::getLayerNo)
                .eq(CStationAreas::getEnable, Status.ENABLED.getValue()));
    }

    @Override
    public CStationAreas getByCode(String code) throws BizException {
        CStationAreas area = getOne(new LambdaQueryWrapper<CStationAreas>().eq(CStationAreas::getCode, code), true);
        if (null == area) throw new BizException(String.format("找不到code[%s]对应的堆存区", code));
        if (Status.DISABLED.getValue().equals(area.getEnable()))
            throw new BizException(String.format("code[%s]对应的堆存区未启用", code));
        return area;
    }

    /**
     * @Author majunzi
     * @Date 2019/8/8
     * @Description 有可用（可用，锁定）堆位的堆区集合，并标注堆位的可用数量和锁定数量
     **/
    @Override
    public List<CStationAreas> selectUseable() {
        List<CStationAreas> areas = list(new LambdaQueryWrapper<CStationAreas>().eq(CStationAreas::getEnable, Status.ENABLED.getValue()));
        List<CStationAreas> useables = new ArrayList<>();

        areas.forEach(area -> {
            int canUse = positionsService.countByStatus(area.getCode(), UseStatus.NOT_USED);
            int preUse = positionsService.countByStatus(area.getCode(), UseStatus.PRE_USED);
            if (canUse > 0 || preUse > 0) {
                area.setRemark(String.format("可用%s,锁定%s", canUse, preUse));
                useables.add(area);
            }
        });
        return useables;
    }

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 保存。编码全局唯一，编码，状态（启用），贝，列，层 必填
     **/
    @Override
    public void saveT(CStationAreas cStationAreas) throws BizException {
        CStationAreas newArea = beforeSave(cStationAreas);
        save(newArea);
    }

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 删除，校验是否有箱位生成，无（可以删除），有（提示：该堆区已配置箱位，无法删除）
     **/
    @Override
    public void delete(String id) throws BizException {
        deleteBefore(id);
        removeById(id);
    }

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 更新 堆区编码不可改
     **/
    @Override
    public void updateT(String id, CStationAreas cStationAreas) {
        CStationAreas newArea = updateBefore(cStationAreas);
        updateById(newArea);
    }

    private CStationAreas updateBefore(CStationAreas cStationAreas) {
        CStationAreas area = cStationAreas.builder().build();
        BeanUtils.copyProperties(cStationAreas, area, "code", "createTime");
        return area;
    }

    private void deleteBefore(String id) throws BizException {
        CStationAreas areas = getById(id);
        if (null == areas) return;
        if (CollectionUtils.isNotEmpty(positionsService.getByAreaCode(areas.getCode())))
            throw new BizException(StringUtils.format("该堆区[%s]已配置箱位，无法删除", areas.getCode()));
    }

    private CStationAreas beforeSave(CStationAreas cStationAreas) throws BizException {
        if (StringUtils.isEmpty(cStationAreas.getCode())) throw new BizException("堆区编码必填");
        if (cStationAreas.getRowNo() == null || cStationAreas.getRowNo() <= 0) throw new BizException("贝必填");
        if (cStationAreas.getColumnNo() == null || cStationAreas.getColumnNo() <= 0) throw new BizException("列必填");
        if (cStationAreas.getLayerNo() == null || cStationAreas.getLayerNo() <= 0) throw new BizException("层必填");
        CStationAreas area = getOne(new LambdaQueryWrapper<CStationAreas>().eq(CStationAreas::getCode, cStationAreas.getCode()), true);
        if (null != area) throw new BizException(String.format("堆区编码[%s]已经存在", cStationAreas.getCode()));
        cStationAreas.setCreateTime(System.currentTimeMillis());
        CStationAreas newArea = CStationAreas.builder().build();
        BeanUtils.copyProperties(cStationAreas, newArea, "id");
        newArea.setEnable(Status.ENABLED.getValue());
        return newArea;
    }

}