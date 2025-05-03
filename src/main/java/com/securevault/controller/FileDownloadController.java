package com.securevault.controller;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Cloudinary;
import com.securevault.model.FileMetadata;
import com.securevault.repository.FileMetadataRepository;

@RestController
@RequestMapping("/api/files")
public class FileDownloadController {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private FileMetadataRepository fileRepo;

    // 🔓 Decrypt and download the file by ID
    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String id) {
        try {
            // 1. Fetch file metadata from MongoDB
            FileMetadata fileMetadata = fileRepo.findById(id).orElseThrow(() -> new RuntimeException("File not found"));

            // 2. Get the AES key from metadata (decrypting the file)
            String aesKey = fileMetadata.getAesKey();
            byte[] decodedKey = Base64.getDecoder().decode(aesKey);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            // 3. Fetch the file from Cloudinary (encrypted file URL)
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) cloudinary.api().resource(fileMetadata.getPublicId(), Map.of("resource_type", "raw"));
            String fileUrl = (String) result.get("url");

            // 4. Download the file from Cloudinary (as an InputStream)
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            try (InputStream inputStream = connection.getInputStream()) {
                byte[] encryptedData = inputStream.readAllBytes();

                // 5. Decrypt the file data
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, originalKey);
                byte[] decryptedData = cipher.doFinal(encryptedData);

                // 6. Return the decrypted file as a download
                ByteArrayResource resource = new ByteArrayResource(decryptedData);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileMetadata.getOriginalFilename())
                        .body(resource);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error downloading or decrypting file: " + e.getMessage());
        }
    }
}
