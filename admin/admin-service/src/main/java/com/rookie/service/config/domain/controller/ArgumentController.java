package com.rookie.service.config.domain.controller;

import com.rookie.api.config.domain.dto.ArgumentAddReqDTO;
import com.rookie.api.config.feign.ArgumentFeignClient;
import com.rookie.commondomain.domain.R;
import com.rookie.service.config.service.ISysArgumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/argument")
public class ArgumentController implements ArgumentFeignClient {
    @Autowired
    private ISysArgumentService sysArgumentService;
    @PostMapping("/add")
    public R<Long> add(@RequestBody ArgumentAddReqDTO argumentAddReqDTO) {
        return R.ok(sysArgumentService.add(argumentAddReqDTO));
    }

}
