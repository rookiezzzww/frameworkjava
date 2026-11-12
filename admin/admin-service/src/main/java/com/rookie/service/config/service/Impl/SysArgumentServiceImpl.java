package com.rookie.service.config.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rookie.api.config.domain.dto.ArgumentAddReqDTO;
import com.rookie.commondomain.exception.ServiceException;
import com.rookie.service.config.domain.entity.SysArgument;
import com.rookie.service.config.mapper.SysArgumentMapper;
import com.rookie.service.config.service.ISysArgumentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysArgumentServiceImpl implements ISysArgumentService {

    @Autowired
    private SysArgumentMapper sysArgumentMapper;


    @Override
    public Long add(ArgumentAddReqDTO argumentAddReqDTO) {
        // 1 查询数据库已有数据
        SysArgument sysArgument = sysArgumentMapper.selectOne(new LambdaQueryWrapper<SysArgument>()
                .eq(SysArgument::getConfigKey, argumentAddReqDTO.getConfigKey())
                .or()
                .eq(SysArgument::getName, argumentAddReqDTO.getName())
        );
        // 2 判断查到的结果，存在抛异常 不存在直接新增
        if (sysArgument != null) {
            throw new ServiceException("参数已存在");
        }
        sysArgument = new SysArgument();
        sysArgument.setName(argumentAddReqDTO.getName());
        sysArgument.setConfigKey(argumentAddReqDTO.getConfigKey());
        sysArgument.setValue(argumentAddReqDTO.getValue());
        if (StringUtils.isNotBlank(argumentAddReqDTO.getRemark())) {
            sysArgument.setRemark(argumentAddReqDTO.getRemark());
        }
        sysArgumentMapper.insert(sysArgument);
        // 3 返回结果
        return sysArgument.getId();

    }
}
