package com.rookie.service.map.service;

import com.rookie.service.map.domain.dto.GeoResultDTO;
import com.rookie.service.map.domain.dto.LocationDTO;
import com.rookie.service.map.domain.dto.PoiListDTO;
import com.rookie.service.map.domain.dto.SuggestSearchDTO;

/**
 * 地图服务提供者
 */
public interface MapProvider {
    /**
     * 地图搜索信息
     * @param suggestSearchDTO 搜索条件
     * @return 搜索结果
     */
    PoiListDTO searchQQMapPlaceByRegion(SuggestSearchDTO suggestSearchDTO);
    /**
     * 根据经纬度获取区划信息
     *
     * @param location 位置信息
     * @return 区划信息
     */
    GeoResultDTO getQQMapDistrictByLonLat(LocationDTO location);
}
