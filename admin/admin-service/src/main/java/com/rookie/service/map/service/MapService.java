package com.rookie.service.map.service;

import com.rookie.api.map.domain.dto.LocationReqDTO;
import com.rookie.api.map.domain.dto.PlaceSearchReqDTO;
import com.rookie.commondomain.domain.dto.BasePageDTO;
import com.rookie.service.map.domain.dto.RegionCityDTO;
import com.rookie.service.map.domain.dto.SearchPoiDTO;
import com.rookie.service.map.domain.dto.SysRegionDTO;

import java.util.List;
import java.util.Map;

/**
 * 地图服务层的接口
 */
public interface MapService {

    /**
     * 城市列表查询
     * mysql版本
     */
    List<SysRegionDTO> getCityListV1();

    /**
     * cache版本
     */
    List<SysRegionDTO> getCityListV2();
    /**
     * 二级缓存版本
     */
    List<SysRegionDTO> getCityListV3();
    /**
     * 缓存预热
     */
    List<SysRegionDTO> getCityList();

    /**
     * 按A-Z归类城市查询
     */
    Map<String, List<SysRegionDTO>> getCityListByAZ();

    /**
     * 获取下级⾏政区划信息
     * @param parentId
     * @return
     */
    List<SysRegionDTO> getRegionChildren(Long parentId);

    /**
     * 获取热⻔城市
     */
    List<SysRegionDTO> getHotCityList();

    /**
     * 根据关键词来搜索地图信息
     * @param placeSearchReqDTO
     * @return
     */
    BasePageDTO<SearchPoiDTO> searchSuggestOnMap(PlaceSearchReqDTO placeSearchReqDTO);
    /**
     * 根据经纬度来定位城市
     * @param locationReqDTO 经纬度信息
     * @return 城市信息
     */
    RegionCityDTO getCityByLocation(LocationReqDTO locationReqDTO);
}

