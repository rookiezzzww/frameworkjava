package com.rookie.service.config.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.api.config.domain.dto.*;
import com.rookie.api.config.domain.vo.DictionaryDataVO;
import com.rookie.api.config.domain.vo.DictionaryTypeVO;
import com.rookie.commondomain.domain.vo.BasePageVO;
import com.rookie.commondomain.exception.ServiceException;
import com.rookie.service.config.domain.entity.SysDictionaryData;
import com.rookie.service.config.domain.entity.SysDictionaryType;
import com.rookie.service.config.mapper.SysDictionaryDataMapper;
import com.rookie.service.config.mapper.SysDictionaryTypeMapper;
import com.rookie.service.config.service.ISysDictionaryService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysDictionaryTypeImpl implements ISysDictionaryService {

    @Autowired
    private SysDictionaryTypeMapper sysDictionaryTypeMapper;

    @Autowired
    private SysDictionaryDataMapper sysDictionaryDataMapper;

    /**
     * 新增字典类型
     */
    @Override
    public Long addType(DictionaryTypeWriteReqDTO dto) {
        LambdaQueryWrapper<SysDictionaryType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysDictionaryType::getId).eq(SysDictionaryType::getTypeKey, dto.getTypeKey()).or().eq(SysDictionaryType::getValue,dto.getValue());
        SysDictionaryType sysDictionaryType = sysDictionaryTypeMapper.selectOne(queryWrapper);
        if(sysDictionaryType != null){
            throw new ServiceException("字典类型已存在");
        }
        SysDictionaryType insert = new SysDictionaryType();
        insert.setTypeKey(dto.getTypeKey());
        insert.setValue(dto.getValue());
        if(StringUtils.isNotBlank(dto.getRemark())){
            insert.setRemark(dto.getRemark());
        }
        sysDictionaryTypeMapper.insert(insert);
        return insert.getId();
    }

    /**
     * 分页查询字典类型
     * @param dto
     * @return
     */
    @Override
    public BasePageVO<DictionaryTypeVO> listType(DictionaryTypeListReqDTO dto) {
        BasePageVO<DictionaryTypeVO> result = new BasePageVO<>();
        //构建查询条件
        LambdaQueryWrapper<SysDictionaryType> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(dto.getTypeKey())){
            queryWrapper.eq(SysDictionaryType::getTypeKey, dto.getTypeKey());
        }
        if(StringUtils.isNotBlank(dto.getValue())){//右模糊查询
            queryWrapper.likeRight(SysDictionaryType::getValue, dto.getValue());
        }
        //分页查询，不然数据量太大
        Page<SysDictionaryType> page = sysDictionaryTypeMapper.selectPage(new Page<>(dto.getPageNo().longValue(), dto.getPageSize().longValue()), queryWrapper);
        //构建返回体
        result.setTotals(Integer.parseInt(String.valueOf(page.getTotal())));//设置总条数
        result.setTotalPages(Integer.parseInt(String.valueOf(page.getPages())));//设置总页数
        List<DictionaryTypeVO> list = new ArrayList<>();
        for (SysDictionaryType sysDictionaryType : page.getRecords()){
            DictionaryTypeVO res = new DictionaryTypeVO();
            BeanUtils.copyProperties(sysDictionaryType, res);
            list.add(res);
        }
        result.setList(list);
        return result;
    }

    /**
     * 编辑字典类型
     */
    @Override
    public Long editType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        //判断字典类型是否存在
        LambdaQueryWrapper<SysDictionaryType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictionaryType::getTypeKey, dictionaryTypeWriteReqDTO.getTypeKey());
        SysDictionaryType sysDictionaryType = sysDictionaryTypeMapper.selectOne(queryWrapper);
        if(sysDictionaryType == null){
            throw new ServiceException("字典类型不存在");
        }
        //判断字典类型名称是否重复
        if (sysDictionaryTypeMapper.selectOne(new LambdaQueryWrapper<SysDictionaryType>()
                .ne(SysDictionaryType::getTypeKey, dictionaryTypeWriteReqDTO.getTypeKey())
                .eq(SysDictionaryType::getValue, dictionaryTypeWriteReqDTO.getValue())
        ) != null) {
            throw new ServiceException("字典类型名称已存在");
        }
        //更新字典类型
        sysDictionaryType.setValue(dictionaryTypeWriteReqDTO.getValue());
        sysDictionaryType.setRemark(dictionaryTypeWriteReqDTO.getRemark());
        sysDictionaryTypeMapper.updateById(sysDictionaryType);
        return sysDictionaryType.getId();
    }

    @Override
    public Long addData(DictionaryDataAddReqDTO dictionaryDataAddReqDTO) {
        if (sysDictionaryTypeMapper.selectOne(new LambdaQueryWrapper<SysDictionaryType>().eq(SysDictionaryType::getTypeKey, dictionaryDataAddReqDTO.getTypeKey())) == null) {
            throw new ServiceException("字典类型不存在");
        }
        SysDictionaryData sysDictionaryData = sysDictionaryDataMapper.selectOne(new LambdaQueryWrapper<SysDictionaryData>()
                .eq(SysDictionaryData::getValue, dictionaryDataAddReqDTO.getValue())
                .or()
                .eq(SysDictionaryData::getDataKey, dictionaryDataAddReqDTO.getDataKey())
        );
        if (sysDictionaryData != null) {
            throw new ServiceException("字典数据键或值已存在");
        }
        sysDictionaryData = new SysDictionaryData();
        sysDictionaryData.setDataKey(dictionaryDataAddReqDTO.getDataKey());
        sysDictionaryData.setTypeKey(dictionaryDataAddReqDTO.getTypeKey());
        sysDictionaryData.setValue(dictionaryDataAddReqDTO.getValue());
        if (dictionaryDataAddReqDTO.getSort() != null) {
            sysDictionaryData.setSort(dictionaryDataAddReqDTO.getSort());
        }
        if (StringUtils.isNotBlank(dictionaryDataAddReqDTO.getRemark())) {
            sysDictionaryData.setRemark(dictionaryDataAddReqDTO.getRemark());
        }
        sysDictionaryDataMapper.insert(sysDictionaryData);
        return sysDictionaryData.getId();
    }

    @Override
    public BasePageVO<DictionaryDataVO> listData(DictionaryDataListReqDTO dto) {
        BasePageVO<DictionaryDataVO> result = new BasePageVO<>();
        LambdaQueryWrapper<SysDictionaryData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictionaryData::getTypeKey, dto.getTypeKey());
        if (StringUtils.isNotBlank(dto.getValue())) {
            queryWrapper.likeRight(SysDictionaryData::getValue, dto.getValue());
        }
        //先按 sort 从小到大排列，`sort` 相同时再按 id 排列，让排序结果更稳定
        queryWrapper.orderByAsc(SysDictionaryData::getSort);
        queryWrapper.orderByAsc(SysDictionaryData::getId);
        Page<SysDictionaryData> page = sysDictionaryDataMapper.selectPage(
                new Page<>(dto.getPageNo().longValue(), dto.getPageSize().longValue()),
                queryWrapper
        );
        result.setTotals(((Long)page.getTotal()).intValue());
        result.setTotalPages(((Long)page.getPages()).intValue());
        List<DictionaryDataVO> list = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : page.getRecords()) {
            DictionaryDataVO dictionaryDataVo = new DictionaryDataVO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataVo);
            list.add(dictionaryDataVo);
        }
        result.setList(list);
        return result;
    }

    @Override
    public Long editData(DictionaryDataAddReqDTO dictionaryDataAddReqDTO) {
        SysDictionaryData sysDictionaryData = sysDictionaryDataMapper.selectOne(new LambdaQueryWrapper<SysDictionaryData>
                        ().eq(SysDictionaryData::getDataKey, dictionaryDataAddReqDTO.getDataKey()));
        if (sysDictionaryData == null) {
            throw new ServiceException("字典数据不存在");
        }
        if (sysDictionaryDataMapper.selectOne(new
                LambdaQueryWrapper<SysDictionaryData>().ne(SysDictionaryData::getDataKey,
                dictionaryDataAddReqDTO.getDataKey()).eq(SysDictionaryData::getValue,
                dictionaryDataAddReqDTO.getValue())) != null) {
            throw new ServiceException("字典数据值已存在");
        }
        sysDictionaryData.setValue(dictionaryDataAddReqDTO.getValue());
        if (dictionaryDataAddReqDTO.getSort() != null) {
            sysDictionaryData.setSort(dictionaryDataAddReqDTO.getSort());
        }
        if (StringUtils.isNotBlank(dictionaryDataAddReqDTO.getRemark())) {
            sysDictionaryData.setRemark(dictionaryDataAddReqDTO.getRemark());
        }
        sysDictionaryDataMapper.updateById(sysDictionaryData);
        return sysDictionaryData.getId();
    }
    /**
     * 根据字典类型查询字典数据
     */
    @Override
    public List<DictionaryDataDTO> selectDictDataByType(String typeKey) {
        List<SysDictionaryData> list = sysDictionaryDataMapper.selectList(new LambdaQueryWrapper<SysDictionaryData>()
                .eq(SysDictionaryData::getTypeKey, typeKey));
        List<DictionaryDataDTO> result = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : list) {
            DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
            result.add(dictionaryDataDTO);
        }
        return result;
    }
    /**
     * 根据多个字典类型查询字典数据
     */
    @Override
    public Map<String, List<DictionaryDataDTO>> selectDictDataByTypes(List<String> dictTypeCodes) {
        List<SysDictionaryData> list = sysDictionaryDataMapper.selectList(new
                LambdaQueryWrapper<SysDictionaryData>().in(SysDictionaryData::getTypeKey,
                dictTypeCodes));
        List<DictionaryDataDTO> result = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : list) {
            DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
            result.add(dictionaryDataDTO);
        }
        Map<String, List<DictionaryDataDTO>> map = new HashMap<>();
        for (DictionaryDataDTO dictionaryDataDTO : result) {
            // 如果 map 中不存在当前字典类型，则创建一个新的列表
            if (map.get(dictionaryDataDTO.getTypeKey()) == null) {
                List<DictionaryDataDTO> value = new ArrayList<>();
                value.add(dictionaryDataDTO);
                map.put(dictionaryDataDTO.getTypeKey(), value);
            } else {// 如果 map 中存在当前字典类型，则将当前字典数据添加到列表中
                List<DictionaryDataDTO> value = map.get(dictionaryDataDTO.getTypeKey());
                value.add(dictionaryDataDTO);
            }
        }
        return map;
    }

    /**
     * 根据字典数据业务主键查询字典数据表实体类对象
     */
    @Override
    public DictionaryDataDTO getDicDataByKey(String dataKey) {
        // 1 根据字典数据业务主键查询字典数据表实体类对象
        SysDictionaryData sysDictionaryData = sysDictionaryDataMapper.selectOne(new LambdaQueryWrapper<SysDictionaryData>().eq(SysDictionaryData::getDataKey, dataKey));
        // 2 做对象转换
        DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
        BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
        return dictionaryDataDTO;
    }
    /**
     * 根据字典数据业务主键列表查询字典数据表实体类对象列表
     */
    @Override
    public List<DictionaryDataDTO> getDicDataByKeys(List<String> dataKeys) {
        // 1 根据字典数据业务主键列表查询字典数据表实体类对象列表
        List<SysDictionaryData> list = sysDictionaryDataMapper.selectList(new LambdaQueryWrapper<SysDictionaryData>().in(SysDictionaryData::getDataKey, dataKeys));
        // 2 做列表的对象转换
        List<DictionaryDataDTO> result = new ArrayList<>();
        for (SysDictionaryData sysDictionaryData : list) {
            DictionaryDataDTO dictionaryDataDTO = new DictionaryDataDTO();
            BeanUtils.copyProperties(sysDictionaryData, dictionaryDataDTO);
            result.add(dictionaryDataDTO);
        }
        return result;
    }
}
