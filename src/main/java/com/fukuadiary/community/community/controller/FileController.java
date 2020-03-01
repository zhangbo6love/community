package com.fukuadiary.community.community.controller;

import com.fukuadiary.community.community.dto.FileDTO;
import com.fukuadiary.community.community.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDTO upload(HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestParam(value = "editormd-image-file", required = false) MultipartFile attach,
                          @RequestParam(value = "id", required = false) Long id){

        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Context-Type", "text/json");
            FileDTO fileDTO = fileService.storeImage(attach);
            Thread.sleep(100);
            return fileDTO;
        } catch (Exception e) {

        }

        return null;
    }
}
