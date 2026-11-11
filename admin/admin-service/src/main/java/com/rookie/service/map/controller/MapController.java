package com.rookie.service.map.controller;

import com.rookie.api.map.domain.dto.LocationReqDTO;
import com.rookie.api.map.domain.dto.PlaceSearchReqDTO;
import com.rookie.api.map.domain.vo.RegionCityVo;
import com.rookie.api.map.domain.vo.RegionVO;
import com.rookie.api.map.domain.vo.SearchPoiVo;
import com.rookie.api.map.feign.MapFeignClient;
import com.rookie.commoncore.utils.BeanCopyUtil;
import com.rookie.commondomain.domain.R;
import com.rookie.commondomain.domain.dto.BasePageDTO;
import com.rookie.commondomain.domain.vo.BasePageVO;
import com.rookie.service.map.domain.dto.RegionCityDTO;
import com.rookie.service.map.domain.dto.SearchPoiDTO;
import com.rookie.service.map.domain.dto.SysRegionDTO;
import com.rookie.service.map.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 地图服务控制器
 */
@RestController
@Slf4j
public class MapController implements MapFeignClient {
    @Autowired
    private MapService mapService;

    /**
     * 城市列表查询
     * @return 城市列表信息
     */
    @Override
    public R<List<RegionVO>> getCityList(){
        List<SysRegionDTO> cityList = mapService.getCityListV3();
        List<RegionVO> regionVOList = BeanCopyUtil.copyListProperties(cityList, RegionVO::new);
        return R.ok(regionVOList);
    }

    /**
     * 按A-Z归类城市查询
     * @return
     */
    @Override
    public R<Map<String, List<RegionVO>>> getCityListByAZ() {
        Map<String, List<RegionVO>> result = new LinkedHashMap<>();
        Map<String, List<SysRegionDTO>> cityListByAZ = mapService.getCityListByAZ();
        for (Map.Entry<String, List<SysRegionDTO>> entry : cityListByAZ.entrySet()){
            result.put(entry.getKey(), BeanCopyUtil.copyListProperties(entry.getValue(), RegionVO::new));
        }
        return R.ok(result);
    }

    /**
     * 获取下级⾏政区划信息
     * @param parentId
     * @return
     */
    @Override
    public R<List<RegionVO>> regionChildren(Long parentId) {
        List<RegionVO> result = new ArrayList<>();
        List<SysRegionDTO> regionChildren = mapService.getRegionChildren(parentId);
        result = BeanCopyUtil.copyListProperties(regionChildren, RegionVO::new);
        return R.ok(result);
    }
    /**
     * 获取热⻔城市
     */
    @Override
    public R<List<RegionVO>> getHotCityList() {
        List<SysRegionDTO> hotCityList = mapService.getHotCityList();
        List<RegionVO> regionVOList = BeanCopyUtil.copyListProperties(hotCityList, RegionVO::new);
        return R.ok(regionVOList);
    }

    /**
     * 根据关键词搜索位置信息
     * @param placeSearchReqDTO
     * @return
     */
    @Override
    public R<BasePageVO<SearchPoiVo>> searchSuggestOnMap(PlaceSearchReqDTO placeSearchReqDTO) {
        BasePageDTO<SearchPoiDTO> basePageReqDTO = mapService.searchSuggestOnMap(placeSearchReqDTO);
        BasePageVO<SearchPoiVo> result = new BasePageVO<>();
        BeanUtils.copyProperties(basePageReqDTO, result);
        return R.ok(result);
    }

    @Override
    public R<RegionCityVo> locateCityByLocation(LocationReqDTO location) {
        RegionCityDTO regionCityDTO = mapService.getCityByLocation(location);
        RegionCityVo regionCityVo = new RegionCityVo();
        BeanUtils.copyProperties(regionCityDTO, regionCityVo);
        return R.ok(regionCityVo);
    }
}
