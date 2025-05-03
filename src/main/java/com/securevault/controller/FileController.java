package com.securevault.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cloudinary.Cloudinary;
import com.securevault.model.FileMetadata;
import com.securevault.repository.FileMetadataRepository;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileMetadataRepository fileRepo;

    @Autowired
    private Cloudinary cloudinary;

    // 🔍 Get all files for a user
    @GetMapping
    public List<FileMetadata> getFilesByUser(@RequestParam String username) {
        return fileRepo.findByUsername(username);
    }

    // 📁 Get all files in a specific folder
    @GetMapping("/folder/files")
    public List<FileMetadata> getFilesInFolder(@RequestParam String username, @RequestParam String folder) {
        return fileRepo.findByUsernameAndFolder(username, folder);
    }

    // 📂 Get all distinct folder names for a user
    @GetMapping("/folders")
    public List<String> getFoldersForUser(@RequestParam String username) {
        List<FileMetadata> folders = fileRepo.findFoldersByUsername(username);
        return folders.stream()
                .map(FileMetadata::getFolder)
                .filter(folder -> folder != null && !folder.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    // ❌ Delete a file by ID
    @DeleteMapping("/{id}")
    public String deleteFile(@PathVariable String id) {
        FileMetadata file = fileRepo.findById(id).orElse(null);
        if (file == null) return "File not found";

        try {
            cloudinary.uploader().destroy(file.getPublicId(), Map.of("resource_type", "raw"));
            fileRepo.deleteById(id);
            return "File deleted successfully";
        } catch (java.io.IOException ioException) {
            return "IO error: " + ioException.getMessage();
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

    // ⭐ Toggle favorite status of a file
    @PatchMapping("/favorite/{id}")
    public String toggleFavorite(@PathVariable String id) {
        FileMetadata file = fileRepo.findById(id).orElse(null);
        if (file == null) return "File not found";

        file.setFavorite(!file.isFavorite());
        fileRepo.save(file);
        return "Favorite status updated";
    }
}
