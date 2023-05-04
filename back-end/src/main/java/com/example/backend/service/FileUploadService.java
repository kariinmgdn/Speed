package com.example.backend.service;

import com.example.backend.FileStorageProperties;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileUploadService {
    private final Path fileUploadLocation;

    @Autowired
    public FileUploadService(FileStorageProperties fileStorageProperties) {
        this.fileUploadLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileUploadLocation);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create the directory where you want to the uploaded the files will be kept.", e);
        }
    }

    public String uploadFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("File name is containing invalid path sequence " + fileName);
            }
            // Copy file to the target path (replacing existing file with the same name)
            Path targetLocation = this.fileUploadLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleteFiles() {
        try {
            Path path = Path.of(this.fileUploadLocation.toUri());
            File folder = new File(path.toUri());
            FileUtils.cleanDirectory(folder);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getFilePath() {
        Path path = Path.of(this.fileUploadLocation.toUri());
        File[] folder = new File(path.toUri()).listFiles();
        assert folder != null;
        return folder.length > 0 ? folder[0].getPath() : null;
    }
}
