package com.rookie.service.config.service;

import com.rookie.api.config.domain.dto.*;
import com.rookie.api.config.domain.vo.DictionaryDataVO;
import com.rookie.api.config.domain.vo.DictionaryTypeVO;
import com.rookie.commondomain.domain.vo.BasePageVO;

import java.util.List;
import java.util.Map;

/**
 * 字典服务接口
 */
public interface ISysDictionaryService {
    /**
     * 新增字典类型
     */
    Long addType(DictionaryTypeWriteReqDTO dto);

    /**
     *字典类型查询
     */
    BasePageVO<DictionaryTypeVO> listType(DictionaryTypeListReqDTO dto);

    /**
     * 编辑字典类型
     */
    Long editType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);

    /**
     * 新增字典数据
     */
    Long addData(DictionaryDataAddReqDTO dictionaryDataAddReqDTO);

    /**
     * 查询字典数据
     */
    BasePageVO<DictionaryDataVO> listData(DictionaryDataListReqDTO dto);

    /**
     * 编辑字典数据
     */
    Long editData(DictionaryDataAddReqDTO dictionaryDataAddReqDTO);
    /**
     * 根据类型查询字典数据
     */
    List<DictionaryDataDTO> selectDictDataByType(String typeKey);
    /**
     * 查多个字典类型下所有的字典数据
     */
    Map<String, List<DictionaryDataDTO>> selectDictDataByTypes(List<String> typeKeys);
    /**
     * 根据字典数据key查询字典数据对象
     */
    DictionaryDataDTO getDicDataByKey(String dataKey);

    /**
     * 根据字典数据keys查询字典数据对象
     */
    List<DictionaryDataDTO> getDicDataByKeys(List<String> dataKeys);
}


