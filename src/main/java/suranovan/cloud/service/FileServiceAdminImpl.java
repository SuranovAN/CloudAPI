package suranovan.cloud.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import suranovan.cloud.model.response.CloudFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileServiceAdminImpl implements IFileService {

    @Value("${cloud.upload.path}")
    private final String uploadPath;

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
        return null;
    }

    @Override
    public ResponseEntity<InputStreamResource> getFileFromCloud(String fileName) throws IOException {
        return null;
    }

    @Override
    public void deleteFile(String fileName) {
        var dir = fileName.split("-");
        File filePath = new File(uploadPath + File.separator + String.join(File.separator, dir));
        filePath.delete();
    }

    @Override
    public void renameFile(String fileName, String newFileName) throws IOException {

    }
}
