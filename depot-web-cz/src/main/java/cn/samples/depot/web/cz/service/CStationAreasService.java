package cn.samples.depot.web.cz.service;

import cn.samples.depot.common.exception.BizException;
import cn.samples.depot.web.entity.CStationAreas;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


/**
 * @Author majunzi
 * @Date 2019/10/15
 * @Description 堆区
 **/
public interface CStationAreasService extends IService<CStationAreas> {
    /**
     * @Author majunzi
     * @Date 2019/8/6
     * @Description 根据code 获取 启用状态的 堆存区， todo 忽略了 station_id
     **/
    CStationAreas getByCode(String code) throws BizException;

    List<Map<String, Object>> select();

    /**
     * @Author majunzi
     * @Date 2019/8/6
     * @Description 下拉列表
     **/
    List<CStationAreas> selectUseable();

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 保存。编码全局唯一，编码，状态（启用），贝，列，层 必填
     **/
    void saveT(CStationAreas cStationAreas) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 删除，校验是否有箱位生成，无（可以删除），有（提示：该堆区已配置箱位，无法删除）
     **/
    void delete(String id) throws BizException;

    /**
     * @Author majunzi
     * @Date 2019/10/15
     * @Description 更新 堆区编码不可改
     **/
    void updateT(String id, CStationAreas cStationAreas);
}