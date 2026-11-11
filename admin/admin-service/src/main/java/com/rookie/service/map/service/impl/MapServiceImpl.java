package com.rookie.service.map.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import com.rookie.api.map.domain.dto.LocationReqDTO;
import com.rookie.api.map.domain.dto.PlaceSearchReqDTO;
import com.rookie.commoncache.utils.CacheUtil;
import com.rookie.commoncore.utils.PageUtil;
import com.rookie.commondomain.domain.dto.BasePageDTO;
import com.rookie.commonredis.service.RedisService;
import com.rookie.api.map.constants.MapConstants;
import com.rookie.service.map.domain.dto.*;
import com.rookie.service.map.domain.entity.SysRegion;
import com.rookie.service.map.mapper.RegionMapper;
import com.rookie.service.map.service.MapProvider;
import com.rookie.service.map.service.MapService;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;


/**
 * 地图服务层的实现类
 */
@Service
public class MapServiceImpl implements MapService {

    /**
     * sys_region表的Mapper
     */
    @Autowired
    private RegionMapper regionMapper;
    /**
     * redis服务
     */
    @Autowired
    private RedisService redisService;
    /**
     * 二级缓存
     */
    @Autowired
    private Cache<String, Object> caffeineCache;
    /**
     * 腾讯位置服务的服务类
     */
    @Autowired
    private MapProvider mapProvider;

    @PostConstruct
    public void init() {
        LoadCityInfo();
        loadCityInfoByAZ();
    }
    /**
     * 获取城市列表
     * 方案一：数据库查询
     * @return 城市列表
     */
    @Override
    public List<SysRegionDTO> getCityListV1() {
        //1.声明一个空列表
        List<SysRegionDTO> result  = new ArrayList<>();
        //2.查询数据库
        List<SysRegion> list = regionMapper.selectAllRegion();
        //3.数据转换
        for (SysRegion region : list){
            if (region.getLevel() == MapConstants.CITY_LEVEL){
                SysRegionDTO city = new SysRegionDTO();
                BeanUtils.copyProperties(region, city);
                result.add(city);
            }
        }
        //4.返回转换后的列表
        return result;
    }

    /**
     * 获取城市列表(缓存)
     * 方案二：redis缓存
     * @return
     */
    @Override
    public List<SysRegionDTO> getCityListV2() {
        //1.声明一个空列表
        List<SysRegionDTO> result  = new ArrayList<>();
        //2.查询redis缓存
        List<SysRegionDTO> cache = redisService.getCacheObject(MapConstants.CACHE_MAP_CITY_KEY, new TypeReference<List<SysRegionDTO>>() {});
        if (cache != null){
            return cache;
        }
        //3.查询数据库
        List<SysRegion> list = regionMapper.selectAllRegion();
        //4.数据转换
        for (SysRegion region : list){
            if (region.getLevel() == MapConstants.CITY_LEVEL){
                SysRegionDTO city = new SysRegionDTO();
                BeanUtils.copyProperties(region, city);
                result.add(city);
            }
        }
        //放入redis缓存
        redisService.setCacheObject(MapConstants.CACHE_MAP_CITY_KEY, result);
        //4.返回转换后的列表
        return result;
    }
    /**
     * 二级缓存：redis + caffeine
     */
    @Override
    public List<SysRegionDTO> getCityListV3() {
        //1.声明一个空列表
        List<SysRegionDTO> result  = new ArrayList<>();
        //2.查询二级缓存
        List<SysRegionDTO> cache = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY, new TypeReference<List<SysRegionDTO>>() {}, caffeineCache);
        if (cache != null){
            return cache;
        }
        //3.查询数据库
        List<SysRegion> list = regionMapper.selectAllRegion();
        //4.数据转换
        for (SysRegion region : list){
            if (region.getLevel() == MapConstants.CITY_LEVEL){
                SysRegionDTO city = new SysRegionDTO();
                BeanUtils.copyProperties(region, city);
                result.add(city);
            }
        }
        //放入二级缓存
        CacheUtil.setL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY, result, caffeineCache,120L, TimeUnit.MINUTES);
        //4.返回转换后的列表
        return result;
    }
    /**
     * 方案四：缓存预热
     */
    private void LoadCityInfo() {
        //1.查询数据库
        List<SysRegion> list = regionMapper.selectAllRegion();
        List<SysRegionDTO> result = new ArrayList<>();
        //2.数据转换
        for (SysRegion region : list){
            if (region.getLevel() == MapConstants.CITY_LEVEL){
                SysRegionDTO city = new SysRegionDTO();
                BeanUtils.copyProperties(region, city);
                result.add(city);
            }
        }
        //3.放入二级缓存
        CacheUtil.setL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY, result, caffeineCache,120L, TimeUnit.MINUTES);
    }
    @Override
    public List<SysRegionDTO> getCityList() {
        List<SysRegionDTO> result = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY, new TypeReference<List<SysRegionDTO>>() {}, caffeineCache);
        return result;
    }

    /**
     * 根据字母获取城市列表
     * @return
     */
    @Override
    public Map<String, List<SysRegionDTO>> getCityListByAZ(){
        Map<String, List<SysRegionDTO>> result = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_CITY_PINYIN_KEY, new TypeReference<Map<String, List<SysRegionDTO>>>() {}, caffeineCache);
       return result;
    }


    /**
     * 初始化A-Z归类城市列表缓存内容
     */
    private void loadCityInfoByAZ() {
        //1.查询数据库
        List<SysRegion> list = regionMapper.selectAllRegion();
        //2.数据转换
        Map<String, List<SysRegionDTO>> result = new TreeMap<>();
        for (SysRegion sysRegion : list) {
            if (sysRegion.getLevel().equals(MapConstants.CITY_LEVEL)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                // 2 ⾸字⺟⼤写拿出来
                String firstChar = sysRegionDTO.getPinyin().toUpperCase().substring(0 ,1);
                // 3 ⾸字⺟出现过
                if (result.containsKey(firstChar)) {
                    result.get(firstChar).add(sysRegionDTO);
                } else {
                    List<SysRegionDTO> regionDTOList = new ArrayList<>();
                    regionDTOList.add(sysRegionDTO);
                    result.put(firstChar, regionDTOList);
                }
            }
        }
        //3.放入二级缓存
        CacheUtil.setL2Cache(redisService, MapConstants.CACHE_MAP_CITY_PINYIN_KEY, result, caffeineCache,120L, TimeUnit.MINUTES);
    }
    /**
     * 根据 parentId 获取区域子节点列表
     */
    @Override
    public List<SysRegionDTO> getRegionChildren(Long parentId) {
        // 1 ⼊参可以参与构建缓存的key
        String key = MapConstants.CACHE_MAP_CITY_CHILDREN_KEY +parentId;
        // 2 查缓存
        List<SysRegionDTO> resultRegions = CacheUtil.getL2Cache(redisService, key, new TypeReference<List<SysRegionDTO>>() {}, caffeineCache);
        if (resultRegions != null) {
            return resultRegions;
        }
        // 3 查询数据库，把parentId与⼊参相等的数据拿出来
        List<SysRegion> list = regionMapper.selectAllRegion();
        List<SysRegionDTO> result = new ArrayList<>();
        for (SysRegion sysRegion : list) {
            if (sysRegion.getParentId() != null && sysRegion.getParentId().equals(parentId)) {
                SysRegionDTO sysRegionDTO = new SysRegionDTO();
                BeanUtils.copyProperties(sysRegion, sysRegionDTO);
                result.add(sysRegionDTO);
            }
        }
        // 4 设置缓存
        CacheUtil.setL2Cache(redisService, key, result, caffeineCache, 120L,
                TimeUnit.MINUTES);
        return result;
    }
    /**
     * 获取热⻔城市列表
     * @return 城市列表
     */
    @Override
    public List<SysRegionDTO> getHotCityList() {
        // 1 先查缓存
        List<SysRegionDTO> hotCityList = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_HOT_CITY, new TypeReference<List<SysRegionDTO>>() {}, caffeineCache);
        if (hotCityList != null) {
            return hotCityList;
        }
        // 2 设置6个热⻔城市
        String ids = "35,108,234,289,342";
        List<Long> idList = new ArrayList<>();
        for (String num : ids.split(",")) {
            idList.add(Long.parseLong(num));
        }
        // 3 查询热⻔城市结果
        List<SysRegionDTO> result = new ArrayList<>();
        //selectBatchIds是MyBatis-Plus原生的批量查询方法
        for (SysRegion sysRegion : regionMapper.selectBatchIds(idList)) {
            SysRegionDTO sysRegionDTO = new SysRegionDTO();
            BeanUtils.copyProperties(sysRegion, sysRegionDTO);
            result.add(sysRegionDTO);
        }
        // 4 设置缓存
        CacheUtil.setL2Cache(redisService, MapConstants.CACHE_MAP_HOT_CITY,
                result, caffeineCache, 120L, TimeUnit.MINUTES);
        return result;
    }
    /**
     * 根据地点搜索
     * @param placeSearchReqDTO 搜索条件
     * @return 搜索结果
     */
    @Override
    public BasePageDTO<SearchPoiDTO> searchSuggestOnMap(PlaceSearchReqDTO placeSearchReqDTO) {
        // 1 构建查询腾讯位置服务的入参
        SuggestSearchDTO suggestSearchDTO = new SuggestSearchDTO();
        BeanUtils.copyProperties(placeSearchReqDTO, suggestSearchDTO);//keyword和pageSize正确拷贝了
        suggestSearchDTO.setPageIndex(placeSearchReqDTO.getPageNo());
        suggestSearchDTO.setId(String.valueOf(placeSearchReqDTO.getId()));
        // 2 调用地图位置查询接口
        PoiListDTO poiListDTO = mapProvider.searchQQMapPlaceByRegion(suggestSearchDTO);
        // 3 做结果对象转换
        List<PoiDTO> poiDTOList = poiListDTO.getData();
        BasePageDTO<SearchPoiDTO> result = new BasePageDTO<>();
        result.setTotals(poiListDTO.getCount());
        result.setTotalPages(PageUtil.getTotalPages(result.getTotals(), placeSearchReqDTO.getPageSize()));

        List<SearchPoiDTO> pageRes = new ArrayList<>();
        for (PoiDTO poiDTO : poiDTOList) {
            SearchPoiDTO searchPoiDTO = new SearchPoiDTO();
            BeanUtils.copyProperties(poiDTO, searchPoiDTO);
            searchPoiDTO.setLongitude(poiDTO.getLocation().getLng());
            searchPoiDTO.setLatitude(poiDTO.getLocation().getLat());
            pageRes.add(searchPoiDTO);
        }
        result.setList(pageRes);
        return result;
    }

    @Override
    public RegionCityDTO getCityByLocation(LocationReqDTO locationReqDTO) {
        //1.构造入参
        LocationDTO locationDTO = new LocationDTO();
        BeanUtils.copyProperties(locationReqDTO, locationDTO);
        RegionCityDTO result = new RegionCityDTO();
        //2.调用地图服务查询
        GeoResultDTO geoResultDTO = mapProvider.getQQMapDistrictByLonLat(locationDTO);
        if (geoResultDTO != null && geoResultDTO.getResult() != null && geoResultDTO.getResult().getAd_info() != null){
            String cityName = geoResultDTO.getResult().getAd_info().getCity();
            // 3 查缓存
            List<SysRegionDTO> cache = CacheUtil.getL2Cache(redisService, MapConstants.CACHE_MAP_CITY_KEY, new TypeReference<List<SysRegionDTO>>() {}, caffeineCache);
            for (SysRegionDTO sysRegionDTO: cache) {
                if (sysRegionDTO.getFullName().equals(cityName)) {
                    BeanUtils.copyProperties(sysRegionDTO, result);
                    return result;
                }
            }
        }
        return result;
    }

}

