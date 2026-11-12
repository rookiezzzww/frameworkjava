package com.rookie.service.config.service;

import com.rookie.api.config.domain.dto.DictionaryTypeListReqDTO;
import com.rookie.api.config.domain.dto.DictionaryTypeWriteReqDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ISysDictionaryServiceTest {
    @Autowired
    private ISysDictionaryService sysDictionaryService;
    @Test
    @Transactional //可以只测试不加到数据库中
    void addType() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
    }
    @Test
    @Transactional
    void listType() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        dto.setTypeKey("height");
        dto.setValue("高度");
        dto.setRemark("高度配置");
        sysDictionaryService.addType(dto);
        DictionaryTypeListReqDTO dictionaryTypeListReqDTO = new DictionaryTypeListReqDTO();
        Assertions.assertTrue(sysDictionaryService.listType(dictionaryTypeListReqDTO).getList().size() > 0);
        dictionaryTypeListReqDTO.setTypeKey("weight");
        Assertions.assertTrue(sysDictionaryService.listType(dictionaryTypeListReqDTO).getList().size() == 1);
        dictionaryTypeListReqDTO.setTypeKey("weight1");
        Assertions.assertTrue(sysDictionaryService.listType(dictionaryTypeListReqDTO).getList().size() == 0);
    }
    @Test
    @Transactional
    void editType() {
        DictionaryTypeWriteReqDTO dto = new DictionaryTypeWriteReqDTO();
        dto.setTypeKey("weight");
        dto.setValue("重量");
        dto.setRemark("重量配置");
        sysDictionaryService.addType(dto);
        dto.setTypeKey("height");
        dto.setValue("高度");
        dto.setRemark("高度配置");
        sysDictionaryService.addType(dto);
        dto.setTypeKey("weight1");
        dto.setValue("重量1");
        dto.setRemark("重量配置1");
//        Assertions.assertTrue(sysDictionaryService.editType(dto) > 0L);
        dto.setTypeKey("weight");
        dto.setValue("高度");
        dto.setRemark("重量配置1");
        Assertions.assertTrue(sysDictionaryService.editType(dto) > 0L);
    }
}