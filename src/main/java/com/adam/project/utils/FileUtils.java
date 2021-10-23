package com.adam.project.utils;

import com.adam.project.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUtils {

    @Value("${upload.path}")
    private String uploadPath;

    public void saveFile(MultipartFile file, User user) throws IOException {
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String resultFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
        user.setPhoto(resultFileName);
        file.transferTo(new File(uploadPath + "/" + resultFileName));
    }
}

