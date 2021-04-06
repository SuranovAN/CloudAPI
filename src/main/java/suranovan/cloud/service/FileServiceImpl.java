package suranovan.cloud.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import suranovan.cloud.model.response.CloudFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import java.util.stream.Stream;

@Service
public class FileServiceImpl implements IFileService {

    public FileServiceImpl(@Value("cloud.upload.path") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Value("${cloud.upload.path}")
    private final String uploadPath;

    @Override
    public File getUsersDir() {
        var userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new File(uploadPath + userName);
    }

    @Override
    public List<CloudFile> listUsersFiles(int limit) {
        if (!(getUsersDir().listFiles() == null)) {
            List<CloudFile> userFileList = new ArrayList<>();
            Stream.of(getUsersDir().listFiles()).limit(limit)
                    .forEach(file -> userFileList.add(new CloudFile(file.getName(), (int) file.length())));
            return userFileList;
        }

        return Collections.emptyList();
    }

    @Override
    public boolean addFileToCloud(String fileName, MultipartFile file) throws IOException {
        var userDir = getUsersDir();
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            if (!userDir.exists()) {
                userDir.mkdir();
            }
/*
  Можно добавить уникальный идентификатор чтобы на сервере отличать одноимённые файлы.
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile.hashCode() + "-" + fileName;
*/
            file.transferTo(new File(userDir + "/" + fileName));
            return true;
        }
        return false;
    }

    @Override
    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public ResponseEntity<InputStreamResource> getFileFromCloud(String fileName) throws IOException {
        File file = new File(getUsersDir() + "/" + fileName);
        MediaType mime = MediaType.parseMediaType(Files.probeContentType(file.toPath()));
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok().contentType(mime)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentLength(file.length())
                .body(resource);
    }

    @Override
    public void deleteFile(String fileName) {
        File userFileToDelete = new File(getUsersDir() + "/" + fileName);
        userFileToDelete.delete();
    }

    @Override
    public void renameFile(String fileName, String newFileName) throws IOException {
        Path userFileToRename = Path.of(getUsersDir() + "/" + fileName);
        Files.move(userFileToRename, userFileToRename.resolveSibling(getFilePathFromJSON(newFileName)));
    }

    private String getFilePathFromJSON(String json) {
        return json.split("([^\\w.]+)")[2];
    }

    public List<CloudFile> adminAccess() {
        Path adminPath = Path.of(uploadPath);
        var arr = adminPath.toFile().listFiles();
        List<CloudFile> listAllFiles = new ArrayList<>();
        Arrays.stream(arr).forEach(dir -> Arrays.stream(dir.listFiles())
                .forEach(file -> listAllFiles.add(new CloudFile(file.getName(), (int) file.length()))));
        return listAllFiles;
    }
}
