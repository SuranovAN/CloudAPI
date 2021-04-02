package suranovan.cloud.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import suranovan.cloud.model.response.CloudFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IFileService {
    File getUsersDir();

    List<CloudFile> listUsersFiles(int limit);

    boolean addFileToCloud(String fileName, MultipartFile file) throws IOException;

    String getUUID();

    ResponseEntity<InputStreamResource> getFileFromCloud(String fileName) throws IOException;

    void deleteFile(String fileName);

    void renameFile(String fileName, String newFileName) throws IOException;


}
