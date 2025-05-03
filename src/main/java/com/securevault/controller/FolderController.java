package com.securevault.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Cloudinary;
import com.securevault.model.FileMetadata;
import com.securevault.repository.FileMetadataRepository;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    @Autowired
    private FileMetadataRepository fileRepo;

    @Autowired
    private Cloudinary cloudinary;

    // ✅ 1. Create a folder (dummy file entry)
    @PostMapping
    public String createFolder(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String folder = data.get("folder");

        if (username == null || folder == null || folder.trim().isEmpty()) {
            return "Invalid request. Username or folder name missing.";
        }

        // Check if folder already exists for user
        List<FileMetadata> existing = fileRepo.findByUsernameAndFolder(username, folder);
        if (!existing.isEmpty()) {
            return "Folder already exists.";
        }

        // Create a dummy file for the folder
        FileMetadata dummy = new FileMetadata();
        dummy.setUsername(username);
        dummy.setFolder(folder);
        dummy.setOriginalFilename("placeholder.txt");
        dummy.setSize(0L);
        dummy.setFavorite(false);
        dummy.setUploadedAt(LocalDateTime.now()); // ✅ Fixed method name

        fileRepo.save(dummy);
        return "Folder created successfully.";
    }

    // 📁 2. Get all files in a specific folder
    @GetMapping("/files")
    public List<FileMetadata> getFilesByFolder(@RequestParam String username, @RequestParam String folder) {
        return fileRepo.findByUsernameAndFolder(username, folder);
    }

    // 📂 3. Get all unique folder names for a user
    @GetMapping("/all")
    public Set<String> getFolders(@RequestParam String username) {
        return fileRepo.findFoldersByUsername(username)
            .stream()
            .map(FileMetadata::getFolder)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    // ❌ 4. Delete a file by ID
    @DeleteMapping("/files/{id}")
    public String deleteFile(@PathVariable String id) {
        FileMetadata file = fileRepo.findById(id).orElse(null);
        if (file == null) return "File not found";
        try {
            cloudinary.uploader().destroy(file.getPublicId(), Map.of("resource_type", "raw"));
            fileRepo.deleteById(id);
            return "File deleted successfully.";
        } catch (java.io.IOException ioException) {
            return "IO error: " + ioException.getMessage();
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

    // ⭐ 5. Toggle favorite status
    @PatchMapping("/files/favorite/{id}")
    public String toggleFavorite(@PathVariable String id) {
        FileMetadata file = fileRepo.findById(id).orElse(null);
        if (file == null) return "File not found";
        file.setFavorite(!file.isFavorite());
        fileRepo.save(file);
        return "Favorite status updated";
    }
}
