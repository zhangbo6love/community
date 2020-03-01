package com.fukuadiary.community.community.service;

import com.fukuadiary.community.community.dto.FileDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.CommunicationException;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {


    @Value("${file.imageUrl}")
    private String imageUrl;

    @Value("${file.save.imageUrl}")
    private String saveToImageUrl;

    public FileDTO storeImage(MultipartFile multipartFile) {
        FileDTO fileDTO = new FileDTO();
        File serverFile = new File(saveToImageUrl);
        if(!serverFile.exists()){
            serverFile.mkdirs();
        }

        String[] list = serverFile.list();
        String imageName = multipartFile.getOriginalFilename();
        for (String str : list) {
            if (multipartFile.getOriginalFilename().equals(str)) {
                imageName = UUID.randomUUID().toString() + multipartFile.getOriginalFilename();
                break;
            }
        }
        File clientFile = new File(serverFile + File.separator + imageName);

        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), clientFile);
        } catch (Exception e) {
        }
        fileDTO.setSuccess(1);
        fileDTO.setMessage("成功");
        fileDTO.setUrl(imageUrl + imageName);
//        fileDTO.setUrl("/img/zzf003.jpg");
        return fileDTO;


    }
}
