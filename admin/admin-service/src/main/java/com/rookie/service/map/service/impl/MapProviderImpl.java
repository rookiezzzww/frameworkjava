package com.rookie.service.map.service.impl;

import com.rookie.api.map.constants.MapConstants;
import com.rookie.commondomain.domain.ResultCode;
import com.rookie.commondomain.exception.ServiceException;
import com.rookie.service.map.domain.dto.GeoResultDTO;
import com.rookie.service.map.domain.dto.LocationDTO;
import com.rookie.service.map.domain.dto.PoiListDTO;
import com.rookie.service.map.domain.dto.SuggestSearchDTO;
import com.rookie.service.map.service.MapProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 地图服务实现类
 */
@Slf4j
@Service
@RefreshScope
@Data
@ConditionalOnProperty(value = "map.type", havingValue = "qqmap")
public class MapProviderImpl implements MapProvider {
    /**
     * 腾讯地图api地址
     */
    @Value("${qqmap.apiServer}")
    private String apiServer;

    /**
     * 调用腾讯位置服务的秘钥
     */
    @Value("${qqmap.key}")
    private String key;

    /**
     * restTemplate: 用于发送http请求和接收响应结果，就是一个web客户端
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 根据关键词搜索地图信息
     * @param suggestSearchDTO 搜索条件
     * @return
     */
    @Override
    public PoiListDTO searchQQMapPlaceByRegion(SuggestSearchDTO suggestSearchDTO) {
        //1.构建请求url
        String url = String.format(apiServer+ MapConstants.QQMAP_API_PLACE_SUGGESTION + "?key=%s&region=%s&region_fix=1&page_index=%s&page_size=%s&keyword=%s", key, suggestSearchDTO.getId(), suggestSearchDTO.getPageIndex(), suggestSearchDTO.getPageSize(), suggestSearchDTO.getKeyword());
        //2.发送请求,获取响应结果,转换为对象
        ResponseEntity<PoiListDTO> response  = restTemplate.getForEntity(url, PoiListDTO.class);
        if (!response.getStatusCode().is2xxSuccessful()){
            log.error("调用腾讯位置服务失败，响应状态码：{}", response.getStatusCode());
            throw new ServiceException(ResultCode.QQMAP_QUERY_FAILED);
        }
        return response.getBody();
    }

    /**
     * 根据经纬度获取区划信息
     * @param location 位置信息
     * @return 区划信息
     */
    @Override
    public GeoResultDTO getQQMapDistrictByLonLat(LocationDTO location) {
        //1.构建请求url
        String url = String.format(apiServer+ MapConstants.QQMAP_GEOCODER + "?key=%s&location=%s&get_poi=1", key, location.formatInfo());
        //2.发送请求,获取响应结果,转换为对象
        ResponseEntity<GeoResultDTO> response  = restTemplate.getForEntity(url, GeoResultDTO.class);
        if (!response.getStatusCode().is2xxSuccessful()){
            log.error("调用腾讯位置服务失败，响应状态码：{}", response.getStatusCode());
            throw new ServiceException(ResultCode.QQMAP_QUERY_FAILED);
        }
        return response.getBody();
    }
}
