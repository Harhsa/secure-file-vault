package com.securevault.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.securevault.model.FileMetadata;
import com.securevault.repository.FileMetadataRepository;

@Service
public class FileUploadService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private FileMetadataRepository fileRepo;

    public String uploadFile(MultipartFile file, String username, String folder) {
        try {
            // 1. Generate AES Key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            String aesKey = Base64.getEncoder().encodeToString(keyBytes);

            // 2. Encrypt file content
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(file.getBytes());

            // 3. Upload encrypted file to Cloudinary
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                encryptedData,
                ObjectUtils.asMap(
                    "resource_type", "raw",
                    "public_id", "vault_" + System.currentTimeMillis()
                )
            );

            // 4. Save metadata to MongoDB
            FileMetadata metadata = new FileMetadata();
            metadata.setUsername(username != null ? username : "guest");
            metadata.setOriginalFilename(file.getOriginalFilename());
            metadata.setFileUrl(uploadResult.get("secure_url").toString());
            metadata.setAesKey(aesKey);
            metadata.setPublicId(uploadResult.get("public_id").toString());
            metadata.setSize(file.getSize());
            metadata.setUploadedAt(LocalDateTime.now());
            metadata.setFolder(folder); // ✅ Save folder info

            fileRepo.save(metadata);

            return "File uploaded and encrypted successfully!";
        } catch (java.security.NoSuchAlgorithmException | javax.crypto.NoSuchPaddingException | java.security.InvalidKeyException e) {
            return "Encryption setup failed: " + e.getMessage();
        } catch (java.io.IOException e) {
            return "File processing failed: " + e.getMessage();
        } catch (javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException e) {
            return "Encryption failed: " + e.getMessage();
        } catch (java.lang.RuntimeException e) {
            return "Cloud upload failed: " + e.getMessage();
        }
    }
}
