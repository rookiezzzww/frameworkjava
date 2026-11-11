package com.rookie.service.map.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rookie.service.map.domain.entity.SysRegion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 操作sys_region数据表的Mapper类
 */
@Mapper
public interface RegionMapper extends BaseMapper<SysRegion> {
    /**
     * 查询全量区域
     */
    List<SysRegion> selectAllRegion();
}
