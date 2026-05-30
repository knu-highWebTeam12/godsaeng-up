package com.godsaeng.godsaeng_up.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class FileStore {
    private static final String MISSION_UPLOAD_URL_PREFIX = "/uploads/mission/";

    @Value("${file.dir}")
    private String fileDir;

    public String storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        Files.createDirectories(Path.of(fileDir));

        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return storeFileName;
    }

    public String getFileUrl(String storeFileName) {
        return MISSION_UPLOAD_URL_PREFIX + storeFileName;
    }

    public String getFileDir() {
        return fileDir;
    }

    private String getFullPath(String filename) {
        return fileDir + filename;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        if (pos < 0) {
            throw new IllegalArgumentException("파일 확장자를 확인할 수 없습니다.");
        }
        return originalFilename.substring(pos + 1);
    }
}
