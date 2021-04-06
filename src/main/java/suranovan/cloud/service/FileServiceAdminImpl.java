package suranovan.cloud.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import suranovan.cloud.model.response.CloudFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileServiceAdminImpl implements IFileService {

    @Value("${cloud.upload.path}")
    private final String uploadPath;

    private File getFilePath(String fileName) {
        var dir = fileName.split("-");
        return new File(uploadPath + File.separator + String.join(File.separator, dir));
    }

    public FileServiceAdminImpl(@Value("cloud.upload.path") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public File getUsersDir() {
        return null;
    }

    @Override
    public List<CloudFile> listUsersFiles(int limit) {
        List<CloudFile> userFileList = new ArrayList<>();
        File path = new File(uploadPath);
        Stream.of(path.listFiles()).limit(limit)
                .forEach(dir -> Stream.of(dir.listFiles())
                        .forEach(file -> userFileList.add(
                                new CloudFile((dir.getName() + "-" + file.getName()), (int) file.length()))));
        return userFileList;
    }

    @Override
    public boolean addFileToCloud(String fileName, MultipartFile file) throws IOException {
        return false;
    }

    @Override
    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public ResponseEntity<InputStreamResource> getFileFromCloud(String fileName) throws IOException {
        File filePath = getFilePath(fileName);
        MediaType mime = MediaType.parseMediaType(Files.probeContentType(filePath.toPath()));
        InputStreamResource resource = new InputStreamResource(new FileInputStream(filePath));

        return ResponseEntity.ok().contentType(mime)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filePath.getName())
                .contentLength(filePath.length())
                .body(resource);
    }

    @Override
    public void deleteFile(String fileName) {
        File filePath = getFilePath(fileName);
        filePath.delete();
    }

    @Override
    public void renameFile(String fileName, String newFileName) throws IOException {
        File filePath = getFilePath(fileName);
        Files.move(filePath.toPath(), filePath.toPath().resolveSibling(getFilePathFromJSON(newFileName)));
    }

    private String getFilePathFromJSON(String json) {
        return json.split("([^\\w.]+)")[2];
    }
}
