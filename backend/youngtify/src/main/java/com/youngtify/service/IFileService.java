package com.youngtify.service;

import com.youngtify.message.BaseResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface IFileService {
    BaseResponse uploadImages(List<MultipartFile> files);
}
