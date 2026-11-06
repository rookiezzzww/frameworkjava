package com.rookie.file.controller;


import com.rookie.commoncore.utils.BeanCopyUtil;
import com.rookie.commoncore.utils.JsonUtil;
import com.rookie.commondomain.domain.R;
import com.rookie.file.domain.dto.FileDTO;
import com.rookie.file.domain.dto.SignDTO;
import com.rookie.file.domain.vo.FileVO;
import com.rookie.file.domain.vo.SignVO;
import com.rookie.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    @PostMapping("/upload")
    public R<FileVO> upload(MultipartFile file) {
        FileDTO uploadDTO = fileService.upload(file);
        FileVO fileVO = new FileVO();
        BeanCopyUtil.copyProperties(uploadDTO, fileVO);
        return R.ok(fileVO);
    }
    @GetMapping("/sign")
    public R<SignVO> getSign() {
        SignDTO signDTO = fileService.getSign();
        SignVO signVO = new SignVO();
        BeanCopyUtil.copyProperties(signDTO, signVO);
        return R.ok(signVO);
    }
}
