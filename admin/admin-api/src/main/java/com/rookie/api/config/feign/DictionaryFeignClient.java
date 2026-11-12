package com.rookie.api.config.feign;

import com.rookie.api.config.domain.dto.DictionaryDataDTO;
import com.rookie.api.config.domain.dto.DictionaryDataListReqDTO;
import com.rookie.api.config.domain.dto.DictionaryTypeListReqDTO;
import com.rookie.api.config.domain.vo.DictionaryDataVO;
import com.rookie.api.config.domain.vo.DictionaryTypeVO;
import com.rookie.commondomain.domain.R;
import com.rookie.commondomain.domain.vo.BasePageVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(contextId = "dictionaryFeignClient", value = "bite-admin")
public interface DictionaryFeignClient {
    /**
     * 根据条件查询字典类型
     * @param dto
     * @return
     */
    R<BasePageVO<DictionaryTypeVO>> listType(DictionaryTypeListReqDTO dto);

    /**
     * 查询字典数据
     * @param dto
     * @return
     */
    R<BasePageVO<DictionaryDataVO>> listData(DictionaryDataListReqDTO dto);

    /**
     * 根据字典类型查询类型下的字典数据
     */
    @GetMapping("/dictionary_data/type")
    List<DictionaryDataDTO> selectDictDataByType(@RequestParam String typeKey);
    /**
     * 查多个字典类型下所有的字典数据
     */
    @PostMapping("/dictionary_data/types")
    Map<String, List<DictionaryDataDTO>> selectDictDataByTypes(@RequestBody List<String> typeKeys);
    /**
     * 根据字典数据key查询字典数据对象
     */
    @GetMapping("/dictionary_data/key")
    DictionaryDataDTO getDicDataByKey(@RequestParam String dataKey);
    /**
     * 根据字典数据keys查询字典数据对象
     */
    @PostMapping("/dictionary_data/keys")
    List<DictionaryDataDTO> getDicDataByKeys(@RequestBody List<String> dataKeys);
}
