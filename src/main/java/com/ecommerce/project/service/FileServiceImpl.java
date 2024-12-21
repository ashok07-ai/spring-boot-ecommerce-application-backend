package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    /**
     * Uploads an image to the specified directory and returns the generated unique file name.
     *
     * @param path The directory path where the file will be uploaded.
     * @param file The MultipartFile object containing the image file data.
     * @return The unique file name of the uploaded image.
     * @throws IOException If an I/O error occurs during file upload.
     */
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Get the original file name from the uploaded file
        String originalFileName = file.getOriginalFilename();

        // Generate a random UUID and append the file extension to create a unique file name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));

        // Build the complete file path for the uploaded image
        String filePath = path + File.separator + fileName;

        // Ensure the directory exists; create it if it does not
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Copy the contents of the uploaded file to the target location
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // Return the unique file name of the uploaded image
        return fileName;
    }
}
