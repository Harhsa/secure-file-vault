package com.securevault.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("files")
public class FileMetadata {
    @Id
    private String id;

    private String username;
    private String originalFilename;
    private String fileUrl;
    private String aesKey;
    private String publicId; // Cloudinary public ID for deletion

    private long size; // File size in bytes
    private LocalDateTime uploadedAt; // Upload timestamp

    private boolean favorite; // New field to track favorite status

    private String folder; // New field to store the folder name

    // Favorite status getters/setters
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    // Folder field getters/setters
    /** Returns the folder name this file is assigned to */
    public String getFolder() {
        return folder;
    }

    /** Sets the folder name this file should be assigned to */
    public void setFolder(String folder) {
        this.folder = folder;
    }

    // Alias methods for backward compatibility
    @Deprecated
    public String getFolderName() {
        return folder;
    }

    @Deprecated
    public void setFolderName(String folder) {
        this.folder = folder;
    }
}
