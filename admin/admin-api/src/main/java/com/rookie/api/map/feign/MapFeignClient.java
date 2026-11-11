package com.rookie.api.map.feign;

import com.rookie.api.map.domain.dto.LocationReqDTO;
import com.rookie.api.map.domain.dto.PlaceSearchReqDTO;
import com.rookie.api.map.domain.vo.RegionCityVo;
import com.rookie.api.map.domain.vo.RegionVO;
import com.rookie.api.map.domain.vo.SearchPoiVo;
import com.rookie.commondomain.domain.R;
import com.rookie.commondomain.domain.vo.BasePageVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 地图服务远程调用
 * contextId: 全局唯一id，用于区分不同的FeignClient
 * value: 指定FeignClient的名称,与服务注册与发现中的服务名称一致
 */
@FeignClient(contextId = "mapFeignClient", value = "bite-admin")
public interface MapFeignClient {
    /**
     * 城市里表查询
     * @return
     */
    @GetMapping("/map/city_list")
    R<List<RegionVO>> getCityList();
    /**
     * 按A-Z归类城市查询
     */
    @GetMapping("/map/city_pinyin_list")
    R<Map<String, List<RegionVO>>> getCityListByAZ();
    /**
     * 获取下级⾏政区划信息
     */
    @GetMapping("/map/region_children_list")
    R<List<RegionVO>> regionChildren(@RequestParam Long parentId);
    /**
     * 获取热⻔城市
     */
    @GetMapping("/map/city_hot_list")
    R<List<RegionVO>> getHotCityList();
    /**
     * 根据关键字查询城市
     */
    @PostMapping("/map/search")
    R<BasePageVO<SearchPoiVo>> searchSuggestOnMap(@RequestBody PlaceSearchReqDTO placeSearchReqDTO);

    /**
     * 根据位置信息查询城市
     * @param location
     * @return
     */
    @PostMapping("/map/locate_city_by_location")
    R<RegionCityVo> locateCityByLocation(@RequestBody LocationReqDTO location);
}
