package com.itransition.courseproject.cloudinary;


// Asatbek Xalimjonov 6/18/22 12:24 AM

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudForImage {

    private final Cloudinary cloudinary;

    public String uploadImageToCloud(File file){
        try {
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        }catch (IOException ignored){}
        return null;
    }

}
