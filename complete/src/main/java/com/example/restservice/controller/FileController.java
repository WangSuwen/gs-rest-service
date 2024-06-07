package com.example.restservice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {
    public static final String UPLOAD_DIR = "uploads";

    @PostMapping("/upload")
    public String update(@RequestParam("file") MultipartFile file) throws Exception {
        System.out.println(file.getContentType());
        System.out.println(file.getName());
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        System.out.println(file.getSize());
        
        try {
            String classPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            Path uploadPath = Paths.get(classPath + UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String extention = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String newFileName = new Date().getTime() + extention;
            Path filePath = uploadPath.resolve(newFileName);
            file.transferTo(filePath);

            return filePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }

        /* String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        
        String extention = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String filePath = path + "upload/" + new Date().getTime() + extention;
        System.out.println(filePath);
        File localfile = new File(filePath);
        file.transferTo(localfile); 
        return new FileInfo(localfile.getAbsolutePath());
        */
        
        
    }
    
    @GetMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String filePath = path + "upload/a.json";
        try (
            InputStream inputStream = new FileInputStream(filePath);
            OutputStream outputStream = response.getOutputStream();
        ) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=a.json");
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }
    }

}
