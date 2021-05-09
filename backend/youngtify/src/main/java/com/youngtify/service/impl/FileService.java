package com.youngtify.service.impl;

import com.youngtify.message.BaseResponse;
import com.youngtify.service.IFileService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileService implements IFileService {
    @Override
    public BaseResponse uploadImages(List<MultipartFile> files) {
        return null;
    }
}
