package com.securevault.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.securevault.service.FileUploadService;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public String upload(
        @RequestParam("file") MultipartFile file,
        @RequestParam("username") String username,
        @RequestParam(value = "folder", required = false) String folder
    ) throws IOException {
        try {
            return fileUploadService.uploadFile(file, username, folder);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return "Upload failed: " + e.getMessage();
        } catch (RuntimeException e) {
            return "Runtime error occurred: " + e.getMessage();
        }
    }
}
