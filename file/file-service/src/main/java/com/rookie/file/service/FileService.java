package com.rookie.file.service;

import com.rookie.file.domain.dto.FileDTO;
import com.rookie.file.domain.dto.SignDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileDTO upload(MultipartFile file);

    SignDTO getSign();
}
