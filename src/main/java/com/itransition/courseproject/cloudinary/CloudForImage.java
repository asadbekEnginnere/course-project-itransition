package com.itransition.courseproject.cloudinary;


// Asatbek Xalimjonov 6/18/22 12:24 AM

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudForImage {

    private final Cloudinary cloudinary;

    public String uploadImageToCloud(MultipartFile multipartFile){
        try {
            File fileForServer = convertMultiPartToFile(multipartFile);
            Map uploadResult = cloudinary.uploader().upload(fileForServer, ObjectUtils.emptyMap());
            fileForServer.delete();
            return (String) uploadResult.get("url");
        }catch (IOException ignored){}
        return null;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File("src/main/resources/file/" + file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
