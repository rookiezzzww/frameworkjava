package com.rookie.service.config.domain.controller;

import com.rookie.api.config.domain.dto.*;
import com.rookie.api.config.domain.vo.DictionaryDataVO;
import com.rookie.api.config.domain.vo.DictionaryTypeVO;
import com.rookie.api.config.feign.DictionaryFeignClient;
import com.rookie.commondomain.domain.R;
import com.rookie.commondomain.domain.vo.BasePageVO;
import com.rookie.service.config.service.ISysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 字典服务控制器
 */
@RestController
public class DictionaryController implements DictionaryFeignClient {
    @Autowired
    private ISysDictionaryService sysDictionaryService;

    /**
     * 新增字典类型
     * RequestBody: 让请求参数为json格式
     * Validated: 校验传过去的参数
     * @param dictionaryTypeWriteReqDTO 新增字典类型DTO
     * @return Long
     */
    @PostMapping("/dictionary_type/add")
    public R<Long> addType(@RequestBody @Validated DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        return R.ok(sysDictionaryService.addType(dictionaryTypeWriteReqDTO));
    }

    /**
     * 根据条件查询字典类型
     * @param dto
     * @return
     */
    @Override
    public R<BasePageVO<DictionaryTypeVO>> listType(DictionaryTypeListReqDTO dto) {
        return R.ok(sysDictionaryService.listType(dto));
    }

    /**
     * 编辑字典类型
     * @param dictionaryTypeWriteReqDTO
     * @return
     */
    @PostMapping("/dictionary_type/edit")
    public R<Long> editType(@RequestBody @Validated DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        return R.ok(sysDictionaryService.editType(dictionaryTypeWriteReqDTO));
    }

    /**
     * 新增字典数据
     * @param dictionaryDataAddReqDTO
     * @return
     */
    @PostMapping("/dictionary_data/add")
    public R<Long> addData(@RequestBody @Validated DictionaryDataAddReqDTO dictionaryDataAddReqDTO) {
        return R.ok(sysDictionaryService.addData(dictionaryDataAddReqDTO));
    }
    /**
     * 查询字典数据
     */
    @Override
    public R<BasePageVO<DictionaryDataVO>> listData(DictionaryDataListReqDTO dto) {
        return R.ok(sysDictionaryService.listData(dto));
    }
    /**
     * 编辑字典数据
     */
    @PostMapping("/dictionary_data/edit")
    public R<Long> editData(@RequestBody @Validated DictionaryDataAddReqDTO dictionaryDataAddReqDTO) {
        return R.ok(sysDictionaryService.editData(dictionaryDataAddReqDTO));
    }
    /**
     * 根据类型查询字典数据
     */
    @Override
    public List<DictionaryDataDTO> selectDictDataByType(String typeKey) {
        return sysDictionaryService.selectDictDataByType(typeKey);
    }
    /**
     * 查多个字典类型下所有的字典数据
     */
    @Override
    public Map<String, List<DictionaryDataDTO>> selectDictDataByTypes(List<String> typeKeys) {
        return sysDictionaryService.selectDictDataByTypes(typeKeys);
    }
    /**
     * 根据字典数据key查询字典数据对象
     */
    @Override
    public DictionaryDataDTO getDicDataByKey(String dataKey) {
        return sysDictionaryService.getDicDataByKey(dataKey);
    }
    /**
     * 根据字典数据keys查询字典数据对象
     */
    @Override
    public List<DictionaryDataDTO> getDicDataByKeys(List<String> dataKeys) {
        return sysDictionaryService.getDicDataByKeys(dataKeys);
    }
}
