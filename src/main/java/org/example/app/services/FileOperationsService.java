package org.example.app.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileOperationsService {

    private final String fileStoragePath = System.getProperty("catalina.home") + File.separator + "external_uploads";

    public String uploadFile(MultipartFile file) throws Exception {
        File dir = new File(fileStoragePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
        file.transferTo(serverFile);
        return serverFile.getAbsolutePath();
    }

    public File getFileToDownload(String fileName) throws FileNotFoundException {
        return new File(fileStoragePath + File.separator + fileName);
    }

    public List<String> getAvailableFileNames() {
        File dir = new File(fileStoragePath);
        return Arrays.asList(dir.list());
    }
}
