package com.securevault.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.securevault.model.FileMetadata;

public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {

    List<FileMetadata> findByUsername(String username);

    List<FileMetadata> findByUsernameAndFolder(String username, String folder);

    List<FileMetadata> findByFolder(String folder);

    @Query(value = "{ 'username': ?0, 'folder': { $ne: null } }", fields = "{ 'folder': 1 }")
    List<FileMetadata> findFoldersByUsername(String username);
}
