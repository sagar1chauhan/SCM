package com.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {


    String getUrlFromPublicId(String publicId);
    String uploadImage(MultipartFile profileImage, String filename);
}
